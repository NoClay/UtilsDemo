package pers.noclay.ecgwaveview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 82661 on 2016/10/29.
 */

public class ECGWaveView extends View {

    private static final String TAG = "HeartWavesView";
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
    private Object lock = new Object();
    OnDataChangedListener onDataChangedListener = null;
    public static final int MSG_UPDATE = 0;

    //定义接口
    public interface OnDataChangedListener {
        void onMaxDataChanged(float max);
        void onAverageDataChanged(float average);
        void onMinDataChanged(float min);
    }

    private void initTimer() {
        if (mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_UPDATE);
            }
        }, 0, 1000 / refreshHz);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE:{
                    invalidate();
                    break;
                }
            }
        }
    };

    /**
     * 在代码中动态生成的时候用
     *
     * @param context
     */
    public ECGWaveView(Context context) {
        this(context, null);
    }

    /**
     * 在布局中使用了自定义属性的时候使用
     *
     * @param context
     * @param attrs
     */
    public ECGWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void resolveAttrs(AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ECGWaveView);
        mTableLineColor = typeArray.getColor(R.styleable.ECGWaveView_tableLineColor, Color.RED);
        mXYTextColor = typeArray.getColor(R.styleable.ECGWaveView_xyTextColor, Color.BLACK);
        mWavesLineColor = typeArray.getColor(R.styleable.ECGWaveView_wavesLineColor, Color.BLACK);
        mXYTextSize = typeArray.getInteger(R.styleable.ECGWaveView_xyTextSize, 20);
        minY = typeArray.getInteger(R.styleable.ECGWaveView_minY, 0);
        maxY = typeArray.getInteger(R.styleable.ECGWaveView_maxY, 4100);
        refreshHz = typeArray.getInteger(R.styleable.ECGWaveView_refreshHZ, 20);
        dataHz = typeArray.getInteger(R.styleable.ECGWaveView_dataHZ, 333);
        xGridNum = typeArray.getInteger(R.styleable.ECGWaveView_xGridNum, 25);
        mTimeCount = typeArray.getFloat(R.styleable.ECGWaveView_mTimeCount, 2f);
        typeArray.recycle();
    }

    /**
     * 在使用了自定义style集的时候使用
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ECGWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        resolveAttrs(attrs);
        initView();
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirstDrawBackground){
            measureGrid();
        }
        Log.d(TAG, "onDraw: draw waves size = " + pointCache.size());
        drawBackground(canvas);
        synchronized (lock){
            drawWaves(canvas);
        }
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
        for (int i = 0; i < pointCache.size() - 2; i++) {
            start = pointCache.get(i);
            end = pointCache.get(i + 1);
            canvas.drawLine(start.x, start.y, end.x, end.y, paintWavesLine);
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
            }else{
                updateData(y);
                //获取数据的平均值和最大值
                synchronized (lock){
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

    private void updateData(float y) {
        if (y > maxData) {
            maxData = y;
            if (this.onDataChangedListener != null) {
                this.onDataChangedListener.onMaxDataChanged(maxData);
            }
        }
        if (y < minData){
            minData = y;
            if (this.onDataChangedListener != null){
                this.onDataChangedListener.onMinDataChanged(minData);
            }
        }
        averageData = (pointCache.size() * averageData + y) / (pointCache.size() + 1);
    }


    private static class PointXY {
        float y;
        float x;
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
        return refreshHz;
    }

    public void setRefreshHz(int refreshHz) {
        this.refreshHz = refreshHz;
        initView();
    }

    public void startRefresh(){
        initTimer();
    }

    public void stopRefresh(){
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

}
