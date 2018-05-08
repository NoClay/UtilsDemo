package pers.noclay.ecgwaveview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by NoClay on 2018/5/8.
 */

public class ECGWaveView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;//绘图的画布
    private boolean mIsDrawing;//控制绘画线程的标志位

    private static final String TAG = "HeartWavesViewV2";
    private int mTableLineColor = Color.RED;
    private int mWavesLineColor = Color.BLACK;
    private int mXYTextColor = Color.BLACK;
    private int mXYTextSize = 20;
    private Context context;
    private Paint paintWavesLine;
    private Paint paintTableLine;
    private TextPaint paintXYText;
    private boolean isFirstDrawPoint = true;
    private boolean isFirstDrawBackground = true;
    private int height;
    private int width;
    private int leftPadding;
    private int rightPadding;
    private int topPadding;
    private int bottomPadding;
    public static final int DEFAULT_PADDING_HORIZONTAL = 100;
    public static final int DEFAULT_PADDING_VERTICAL = 50;
    private int maxY = 4100;
    private int minY = 0;
    //x轴每个小格子对应的秒
    //y轴每个小格子对应的指数
    private float gridSecond = 0.1f;
    private float mTimeCount = 2;
    private int xGridNum = 25;
    private int yGridNum;
    private int gridWidth;
    private float grid_num;
    private int zeroCurY;
    private int yStartNum;
    private int workWidth;
    private int workHeight;
    //几秒钟一次数据，默认为1秒1次
    private int dataHz = 333;
    private int refreshHz = 20;
    private List<PointXY> pointCache;
    private int dataThreshold;
    private float averageData;
    private float maxData;
    private float minData;
    private float dataXOffset;
    private Timer mTimer;
    //定义接口
    public int TIME_IN_FRAME = 30;
    private Object lock = new Object();
    OnDataChangedListener onDataChangedListener = null;

    public static final int MSG_UPDATE = 0;

    public interface OnDataChangedListener {

        void onMaxDataChanged(float max);

        void onAverageDataChanged(float average);

        void onMinDataChanged(float min);


    }


    private void initView() {
        //生成抗锯齿的画笔
        pointCache = new ArrayList<>();
        paintWavesLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔粗细
        paintWavesLine.setStrokeWidth(2.5f);
        //设置画笔颜色
        paintWavesLine.setColor(mWavesLineColor);

        paintTableLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTableLine.setColor(mTableLineColor);
        paintTableLine.setAntiAlias(true);
        paintTableLine.setAlpha(100);
        paintWavesLine.setStrokeWidth(4);

        paintXYText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintXYText.setColor(mXYTextColor);
        paintXYText.setTextSize(mXYTextSize);
        isFirstDrawBackground = true;
    }

    private void measureGrid() {
        height = getHeight();
        width = getWidth();
        leftPadding = getPaddingLeft() > DEFAULT_PADDING_HORIZONTAL ? getPaddingLeft() : DEFAULT_PADDING_HORIZONTAL;
        rightPadding = getPaddingRight() > DEFAULT_PADDING_HORIZONTAL ? getPaddingRight() : DEFAULT_PADDING_HORIZONTAL;
        topPadding = getPaddingTop() > DEFAULT_PADDING_VERTICAL ? getPaddingTop() : DEFAULT_PADDING_VERTICAL;
        bottomPadding = getPaddingBottom() > DEFAULT_PADDING_VERTICAL ? getPaddingBottom() : DEFAULT_PADDING_VERTICAL;
        xGridNum = (int) (mTimeCount / gridSecond);
        xGridNum = xGridNum % 5 == 0 ? xGridNum : (xGridNum % 5 > 3 ? (xGridNum / 5 + 1) * 5 : xGridNum / 5 * 5);
        gridWidth = (width - leftPadding - rightPadding) / xGridNum;
        yGridNum = (height - topPadding - rightPadding) / gridWidth;
        yGridNum = yGridNum % 5 == 0 ? yGridNum : (yGridNum % 5 > 3 ? (yGridNum / 5 + 1) * 5 : yGridNum / 5 * 5);
        //获取工作区的宽和高
        workWidth = gridWidth * xGridNum;
        workHeight = gridWidth * yGridNum;
        //计算数据的阈值
        dataThreshold = (int) (dataHz * mTimeCount);
        dataXOffset = (((float) gridWidth) / ((float) gridSecond)) / ((float) dataHz);
    }

    private void drawWaves(Canvas canvas) {
        PointXY start;
        PointXY end;
        synchronized (lock) {
            for (int i = 0; i < pointCache.size() - 2; i++) {
                start = pointCache.get(i);
                end = pointCache.get(i + 1);
                if (start != null && end != null) {
                    canvas.drawLine(start.x, start.y, end.x, end.y, paintWavesLine);
                }
            }
        }
    }

    private void drawBackground(Canvas canvas) {
        if (isFirstDrawBackground) {
            isFirstDrawBackground = false;
            //获取xy轴比例尺
            //获得y轴0标识位的位置
            if (maxY > 0 && minY >= 0) {
                yStartNum = maxY;
                grid_num = maxY / yGridNum;
                zeroCurY = yGridNum;
            } else if (maxY <= 0 && minY < 0) {
                yStartNum = 0;
                grid_num = -minY / yGridNum;
                zeroCurY = 0;
            } else {
                zeroCurY = yGridNum / 2;
                zeroCurY = zeroCurY % 5 == 0 ? zeroCurY :
                        (zeroCurY % 5 > 3) ? (zeroCurY / 5 + 1) * 5 : (zeroCurY / 5 * 5);
                grid_num = Math.max(maxY, minY) / Math.min(yGridNum - zeroCurY, zeroCurY);
                yStartNum = (int) (zeroCurY * grid_num);
            }
        }
        for (int i = 0; i <= xGridNum; i++) {
            paintTableLine.setStrokeWidth(1f);
            if (i % 5 == 0) {
                paintTableLine.setStrokeWidth(3f);
                String label = gridSecond * i + "";
                canvas.drawText(label,
                        leftPadding + i * gridWidth - mXYTextSize / 2,
                        workHeight + bottomPadding / 2 + topPadding + 10,
                        paintXYText);
            }
            canvas.drawLine(leftPadding + i * gridWidth, topPadding,
                    leftPadding + i * gridWidth, topPadding + workHeight, paintTableLine);
        }
        for (int i = 0; i <= yGridNum; i++) {
            paintTableLine.setStrokeWidth(1f);
            if (i % 5 == 0) {
                paintTableLine.setStrokeWidth(3f);
                String label = yStartNum - i * grid_num + "";
                canvas.drawText(label, leftPadding / 5, topPadding + i * gridWidth, paintXYText);
            }
            canvas.drawLine(leftPadding, topPadding + i * gridWidth,
                    leftPadding + workWidth, topPadding + i * gridWidth, paintTableLine);
        }
//        canvas.drawText(title, width / 2 - mTitleSize * title.length() / 2,
//                topPadding / 2, paintTitle);
    }


    public void drawNextPoint(float y) {
        y = y > maxY ? maxY : y;
        y = y < minY ? minY : y;
        if (!isFirstDrawBackground) {
            if (isFirstDrawPoint) {
                isFirstDrawPoint = false;
                PointXY point = new PointXY();
                point.x = leftPadding;
                point.y = ((yStartNum - y) / grid_num * gridWidth + topPadding);
                pointCache.add(point);
            } else {
                updateData(y);
                //获取数据的平均值和最大值
                synchronized (lock) {
                    PointXY nowPoint = new PointXY();
                    int index = pointCache.size();
                    if (index == dataThreshold - 1) {
                        if (this.onDataChangedListener != null) {
                            this.onDataChangedListener.onAverageDataChanged(averageData);
                        }
                        PointXY last = pointCache.get(index - 1);
                        pointCache.clear();
                        last.x = leftPadding;
                        pointCache.add(last);
                    }
                    nowPoint.x = (dataXOffset * pointCache.size() + leftPadding);
                    Log.d("x", "drawNextPoint: x = " + nowPoint.x);
                    nowPoint.y = ((yStartNum - y) / grid_num * gridWidth + topPadding);
                    pointCache.add(nowPoint);
                }
            }
        }
    }

    private void resolveAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ECGWaveView);
        mTableLineColor = typeArray.getColor(R.styleable.ECGWaveView_tableLineColor, Color.RED);
        mXYTextColor = typeArray.getColor(R.styleable.ECGWaveView_xyTextColor, Color.BLACK);
        mWavesLineColor = typeArray.getColor(R.styleable.ECGWaveView_wavesLineColor, Color.BLACK);
        mXYTextSize = typeArray.getInteger(R.styleable.ECGWaveView_xyTextSize, 20);
        minY = typeArray.getInteger(R.styleable.ECGWaveView_minY, 0);
        maxY = typeArray.getInteger(R.styleable.ECGWaveView_maxY, 4100);
        refreshHz = typeArray.getInteger(R.styleable.ECGWaveView_refreshHZ, 20);
        TIME_IN_FRAME = 1000 / refreshHz;
        dataHz = typeArray.getInteger(R.styleable.ECGWaveView_dataHZ, 333);
        xGridNum = typeArray.getInteger(R.styleable.ECGWaveView_xGridNum, 25);
        mTimeCount = typeArray.getFloat(R.styleable.ECGWaveView_mTimeCount, 2f);
        typeArray.recycle();
    }


    private void updateData(float y) {
        if (y > maxData) {
            maxData = y;
            if (this.onDataChangedListener != null) {
                this.onDataChangedListener.onMaxDataChanged(maxData);
            }
        }
        if (y < minData) {
            minData = y;
            if (this.onDataChangedListener != null) {
                this.onDataChangedListener.onMinDataChanged(minData);
            }
        }
        averageData = (pointCache.size() * averageData + y) / (pointCache.size() + 1);
    }

    private static class PointXY {
        float y;
        float x;

    }

    public ECGWaveView(Context context) {
        this(context, null, 0);
    }

    public ECGWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ECGWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        resolveAttrs(attrs);
        initView();
        initHolder();
    }

    private void initHolder() {
        mHolder = getHolder();//获取SurfaceHolder对象
        mHolder.addCallback(this);//注册SurfaceHolder的回调方法
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            mIsDrawing = false;
        }
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            synchronized (this){
                /**取得更新之前的时间**/
                long startTime = System.currentTimeMillis();
                if (isFirstDrawBackground) {
                    measureGrid();
                }
                /**在这里加上线程安全锁**/
                synchronized (mHolder) {
                    mCanvas = mHolder.lockCanvas();
                    /**拿到当前画布 然后锁定**/
                    draw(mCanvas);
                    /**绘制结束后解锁显示在屏幕上**/
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
                /**取得更新结束的时间**/
                long endTime = System.currentTimeMillis();
                /**计算出一次更新的毫秒数**/
                int diffTime = (int) (endTime - startTime);
                /**确保每次更新时间为30帧**/
                while (diffTime <= TIME_IN_FRAME) {
                    diffTime = (int) (System.currentTimeMillis() - startTime);
                    /**线程等待**/
                    Thread.yield();
                }
            }

        }

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        super.draw(canvas);
        if (getBackground() == null) {
            canvas.drawColor(Color.WHITE);
        } else {
            canvas.drawBitmap(((BitmapDrawable) getBackground()).getBitmap(), 0, 0, new Paint());
        }
        drawBackground(canvas);
        drawWaves(canvas);
    }


    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

    public int getmTableLineColor() {
        return mTableLineColor;
    }

    public void setmTableLineColor(int mTableLineColor) {
        this.mTableLineColor = mTableLineColor;
        initView();
    }

    public int getmWavesLineColor() {
        return mWavesLineColor;
    }

    public void setmWavesLineColor(int mWavesLineColor) {
        this.mWavesLineColor = mWavesLineColor;
        initView();
    }

    public int getmXYTextColor() {
        return mXYTextColor;
    }

    public void setmXYTextColor(int mXYTextColor) {
        this.mXYTextColor = mXYTextColor;
        initView();
    }

    public int getmXYTextSize() {
        return mXYTextSize;
    }

    public void setmXYTextSize(int mXYTextSize) {
        this.mXYTextSize = mXYTextSize;
        initView();
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
        initView();
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
        initView();
    }

    public float getmTimeCount() {
        return mTimeCount;
    }

    public void setmTimeCount(float mTimeCount) {
        this.mTimeCount = mTimeCount;
        initView();
    }

    public int getxGridNum() {
        return xGridNum;
    }

    public void setxGridNum(int xGridNum) {
        this.xGridNum = xGridNum;
        initView();
    }

    public int getDataHz() {
        return dataHz;
    }

    public void setDataHz(int dataHz) {
        this.dataHz = dataHz;
        initView();
    }

    public int getRefreshHz() {
        return 1000 / refreshHz;
    }

    public void setRefreshHz(int refreshHz) {
        this.refreshHz = refreshHz;
        this.TIME_IN_FRAME = 1000 / refreshHz;
        initView();
    }
}
