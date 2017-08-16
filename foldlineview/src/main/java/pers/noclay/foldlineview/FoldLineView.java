package pers.noclay.foldlineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import java.util.List;


/**
 * Created by noclay on 2017/8/13.
 */

public class FoldLineView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "FoldLineView";
    /**
     * surface持有者
     */
    private SurfaceHolder mHolder;
    /**
     * 当前画布
     */
    private Canvas mCanvas;
    /**
     * 是否开始绘画
     */
    private boolean mIsDrawing = false;
    /**
     * 最后一次点击的x坐标
     */
    private int lastX;
    /**
     * 偏移量，用来实现平滑移动
     */
    private int mOffset = 0;
    /**
     * 移动速度，用来实现速度递减
     */
    private int mSpeed = 0;
    /**
     * 是否触摸屏幕
     */
    private boolean mIsTouch = false;
    /**
     * 时间计数器，用来快速滚动时候减速
     */
    private int time = 0;
    /**
     * 移动时候X方向上的速度
     */
    private double xVelocity = 0;
    /**
     * 是否可以滚动，当不在范围时候不可以滚动
     */
    private boolean isScroll = true;
    /**
     * 每个x轴刻度的宽度
     */
    private int mXScaleWidth;
    /**
     * 每个y轴刻度的宽度
     */
    private int mYScaleWidth;
    /**
     * 外部的list，用来存放折线上的值
     */
    private FoldLineAdapter mAdapter;
    /**
     * 左、上、右、下四个内边距
     */
    private Thread drawThread;
    private int offsetMin = 0;
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;
    private int mWidth;
    private int mHeight;
    private float gridX, gridY;
    private Paint backPaint;
    private Paint linePaint;
    private TextPaint textPaint;
    private float mYStart, mYEnd;
    /**
     * x轴上的格子数量
     */
    private int mXScaleNum;
    /**
     * y轴上的格子数量
     */
    private int mYScaleNum;
    private int backLineColor;
    private int lineColor;
    private int labelColor;
    private int labelTextSize;
    private List<Integer> mColors;
    public static final int ERROR = -1;
    public static final int DEFAULT_X_SCALE_NUM = 6;
    public static final int DEFAULT_Y_SCALE_NUM = 9;
    public static final int DEFAULT_BACK_LINE_COLOR = Color.LTGRAY;
    public static final int DEFAULT_LINE_COLOR = Color.BLACK;
    public static final int DEFAULT_LABEL_COLOR = Color.LTGRAY;
    public static final int DEFAULT_LABEL_TEXT_SIZE = 30;
    public static final float DEFAULT_Y_START = -300;
    public static final float DEFAULT_Y_END = 300;
    public OnScrollChartListener onScrollChartListener;
    public interface OnScrollChartListener{
        void onScroll(int left, int right);
    }
    public OnScrollChartListener getOnScrollChartListener() {
        return onScrollChartListener;
    }

    public void setOnScrollChartListener(OnScrollChartListener onScrollChartListener) {
        this.onScrollChartListener = onScrollChartListener;
    }

    public List<Integer> getColors() {
        return mColors;
    }

    public void setColors(List<Integer> colors) {
        mColors = colors;
    }

    /**
     * 构造方法区
     *
     * @param context
     */
    public FoldLineView(Context context) {
        this(context, null);
    }

    public FoldLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttrs(attrs);
        init();
    }

    private void resolveAttrs(AttributeSet attrs) {
        TypedArray type = getContext().obtainStyledAttributes(attrs, R.styleable.FoldLineView);
        backLineColor = type.getColor(R.styleable.FoldLineView_backLineColor, DEFAULT_BACK_LINE_COLOR);
        lineColor = type.getColor(R.styleable.FoldLineView_lineColor, DEFAULT_LINE_COLOR);
        labelColor = type.getColor(R.styleable.FoldLineView_labelColor, DEFAULT_LABEL_COLOR);
        labelTextSize = type.getDimensionPixelSize(R.styleable.FoldLineView_labelSize, DEFAULT_LABEL_TEXT_SIZE);
        mXScaleNum = type.getInteger(R.styleable.FoldLineView_xScaleNum, DEFAULT_X_SCALE_NUM);
        mYScaleNum = type.getInteger(R.styleable.FoldLineView_yScaleNum, DEFAULT_Y_SCALE_NUM);
        mYStart = type.getFloat(R.styleable.FoldLineView_yStart, DEFAULT_Y_START);
        mYEnd = type.getFloat(R.styleable.FoldLineView_yEnd, DEFAULT_Y_END);
        type.recycle();
    }

    /**
     * 对view进行初始化
     */
    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(backLineColor);
        linePaint.setColor(lineColor);
        textPaint.setColor(labelColor);
        textPaint.setTextSize(labelTextSize);
        this.setYRange(-300, 300);
    }

    /**
     * 在surface被创建时
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getMeasure();
    }


    public void getMeasure() {
        mWidth = getWidth();
        mHeight = getHeight();
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();
        mPaddingRight = getPaddingRight();
        mXScaleWidth = (mWidth - mPaddingLeft - mPaddingRight) / mXScaleNum;
        mYScaleWidth = (mHeight - mPaddingTop - mPaddingBottom) / mYScaleNum;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            //进行画图
            // 绘制方法
            getMeasure();
            setSpeedCut();
            setOffsetRange();
            drawing();
        }
    }

    public void startDrawing() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        mIsDrawing = true;
        if (drawThread == null) {
            drawThread = new Thread(this);
            drawThread.start();
        }
    }

    public void stopDrawing() {
        mIsDrawing = false;
        drawThread = null;
    }

    private void drawing() {
        try {
            long start = System.currentTimeMillis();
            // 获取并锁定画布
            mCanvas = mHolder.lockCanvas();
            // 绘制坐标轴
            drawAxis();
//            // 绘制曲线
            drawLine();
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                Thread.sleep(50 - (end - start));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                // 保证每次都将绘制的内容提交到服务器
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }


    private void drawLine() {
        //判定每一条线
        int left = 0;
        int right = 0;
        if (mAdapter.getCount() > 0) {
            left = right = ERROR;
            for (int i = mAdapter.getCount() - 1; i >= 0; i--) {

                int temp = computeX(i);
                if (checkIsInChart(temp)) {
                    //该点在折线内
                    if (right == ERROR) {
                        right = i;
                    }
                    left = i;
                    mCanvas.drawLine(temp, mPaddingTop,
                            temp, mHeight - mPaddingBottom,
                            backPaint);
                    mCanvas.drawText(mAdapter.getItemLabel(i), temp, mHeight - mPaddingBottom / 4, textPaint);
                    List<Float> nowPoints = mAdapter.getItemPoint(i);
                    backPaint.setStrokeWidth(3f);
                    for (int j = 0; j < nowPoints.size(); j++) {
                        if (mColors != null && mColors.size() != 0) {
                            backPaint.setColor(mColors.get(j % mColors.size()));
                        } else {
                            backPaint.setColor(lineColor);
                        }
                        mCanvas.drawCircle(temp, computeY((nowPoints.get(j))), 5, backPaint);
                    }
                    backPaint.setColor(backLineColor);
                    backPaint.setStrokeWidth(1f);
                    linePaint.setStrokeWidth(2f);
                    if (i != 0 && checkIsInChart(computeX(i - 1))) {
                        //上一个点在折线图内,且存在上一个点
                        List<Float> lastPoints = mAdapter.getItemPoint(i - 1);
                        for (int j = 0; j < nowPoints.size(); j++) {
                            if (mColors != null && mColors.size() != 0) {
                                linePaint.setColor(mColors.get(j % mColors.size()));
                            } else {
                                linePaint.setColor(lineColor);
                            }
                            mCanvas.drawLine(computeX(i - 1),
                                    computeY(lastPoints.get(j)),
                                    temp,
                                    computeY(nowPoints.get(j)),
                                    linePaint
                            );
                        }
                        linePaint.setColor(lineColor);
                    }
                }
            }
        }
        onScrollChartListener.onScroll(left, right);
    }

    private int computeY(int data) {
        data = data > mYEnd ? (int) mYEnd : data;
        return (int) (mHeight - mPaddingBottom - mYScaleWidth * ((data - mYStart) / gridY));
    }

    private int computeY(float data) {
        data = data > mYEnd ? (int) mYEnd : data;
        return (int) (mHeight - mPaddingBottom - mYScaleWidth * ((data - mYStart) / gridY));
    }

    /**
     * 计算每一个x的坐标
     *
     * @param i
     * @return
     */
    private int computeX(int i) {
        return (i - mAdapter.getCount()) * mXScaleWidth + mOffset + mWidth - mPaddingRight;
    }

    /**
     * 判断当前坐标点是否在折线图内
     *
     * @param x
     * @return
     */
    private boolean checkIsInChart(int x) {
        if (x > mPaddingLeft && x < mWidth - mPaddingRight) {
            return true;
        }
        return false;
    }

    private void drawAxis() {
        // 设置画布背景为白色
        mCanvas.drawColor(Color.WHITE);
        //开始画边界
        backPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5}, 1));
        backPaint.setStrokeWidth(1f);
        mCanvas.drawLine(mPaddingLeft, mHeight - mPaddingBottom,
                mWidth - mPaddingRight, mHeight - mPaddingBottom, backPaint);
        mCanvas.drawLine(mPaddingLeft, mPaddingTop, mPaddingLeft,
                mHeight - mPaddingBottom, backPaint);
        for (int i = 1; i < mYScaleNum; i++) {
            mCanvas.drawLine(mPaddingLeft, mPaddingTop + i * mYScaleWidth,
                    mWidth - mPaddingRight, mPaddingTop + i * mYScaleWidth, backPaint);
        }
        textPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 1; i <= mYScaleNum; i++) {
            mCanvas.drawText("" + (int) (mYEnd - (i - 1) * gridY),
                    mPaddingLeft / 2, mPaddingTop + i * mYScaleWidth, textPaint);
        }
    }

    /**
     * 设置快速滚动时，末尾的减速
     */
    private void setSpeedCut() {
        if (!mIsTouch && isScroll) {
            // 通过当前速度计算所对应的偏移量
            mOffset = mOffset + mSpeed;
        }
        // 每次偏移量的计算
        if (mSpeed != 0) {
            time++;
            mSpeed = (int) (xVelocity + time * time * (xVelocity / 1600.0) - (xVelocity / 20.0) * time);
        } else {
            time = 0;
            mSpeed = 0;
        }

    }

    /**
     * 对偏移量进行边界值判定
     */
    private void setOffsetRange() {

        int offsetMax = mXScaleWidth * (mAdapter.getCount() - 5);
        if (mOffset >= offsetMax) {
            isScroll = false;
            mOffset = offsetMax;
        } else if (mOffset < offsetMin) {// 如果划出最大值范围
            isScroll = false;
            mOffset = offsetMin;
        } else {
            isScroll = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 计算当前速度
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        // 计算速度的单位时间
        velocityTracker.computeCurrentVelocity(50);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = (int) event.getX();
                mIsTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = (int) (event.getX() - lastX);
                // 在当前偏移量的基础上增加偏移量
                mOffset = mOffset + offsetX;
                setOffsetRange();
                // 偏移量修改后下次重绘会有变化
                lastX = (int) event.getX();
                // 获取X方向上的速度
                xVelocity = velocityTracker.getXVelocity();
                mSpeed = (int) xVelocity;
                break;
            case MotionEvent.ACTION_UP:
                mIsTouch = false;
                break;
        }
        // 计算完成后回收内存
        velocityTracker.clear();
        velocityTracker.recycle();
        return true;
    }


    public float getYStart() {
        return mYStart;
    }

    public void setYRange(float yStart, float yEnd) {
        this.mYStart = yStart;
        this.mYEnd = yEnd;
        this.gridY = (yEnd - yStart) / (mYScaleNum - 1);
    }


    public int getLabelTextSize() {
        return labelTextSize;
    }

    public void setLabelTextSize(int labelTextSize) {
        this.labelTextSize = labelTextSize;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getBackLineColor() {
        return backLineColor;
    }

    public void setBackLineColor(int backLineColor) {
        this.backLineColor = backLineColor;
    }

    public int getYScaleNum() {
        return mYScaleNum;
    }

    public void setYScaleNum(int YScaleNum) {
        mYScaleNum = YScaleNum;
    }

    public int getXScaleNum() {
        return mXScaleNum;
    }

    public void setXScaleNum(int XScaleNum) {
        mXScaleNum = XScaleNum;
    }

    public void setYEnd(float YEnd) {
        mYEnd = YEnd;
    }

    public void setYStart(float YStart) {
        mYStart = YStart;
    }

    public float getYEnd() {
        return mYEnd;
    }

    public FoldLineAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(FoldLineAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * dp转化为px工具
     */
    private int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
