# ECGWaveView

这是一个心电图的自定义View

# 属性

自定义属性如下:

```xml
    <declare-styleable name="ECGWaveView">
        <attr name="tableLineColor" format="color"/>    表格线的颜色
        <attr name="xyTextColor" format="color"/>		xy轴字体的颜色
        <attr name="wavesLineColor" format="color"/>	心电图波浪线的颜色
        <attr name="xyTextSize" format="integer"/>		xy轴字体的大小
        <attr name="minY" format="integer"/>			y轴的最小范围
        <attr name="maxY" format="integer"/>			y轴的最大范围
        <attr name="refreshHZ" format="integer"/>		波形图刷新的频率，注意，频率越高，对cpu消耗越大，建议默认为每秒20次
        <attr name="dataHZ" format="integer"/>			波形图数据采样频率，即每秒内传入多少个点，默认为333，即每秒传入333个点				
        <attr name="xGridNum" format="integer"/>		波形图x轴的格子数，建议为5的倍数，view会自动调整为满足需求的个数
        <attr name="mTimeCount"  format="float"/>		x轴所有格子代表的时间，单位是秒，默认为2秒
    </declare-styleable>
```

# 方法

以上属性均可以通过set，get方法进行获取，此处不再列举

```java
public void startRefresh()
public void stopRefresh()
```

**控制是否刷新视图，注意在方法调用时，start只能启动没有开始的视图，建议在onResume方法中调用startRefresh，在onPause中调用stopRefresh**

```java
public void drawNextPoint(float y)
```

将下一个点的y数据传入即可，可以在子线程中调用

# Listener

```java
    public interface OnDataChangedListener {
        void onMaxDataChanged(float max);
        void onAverageDataChanged(float average);
        void onMinDataChanged(float min);
    }
```

当开始传入数据的时候，会开始统计当前时间段内（默认是2s）最大值，平均值，最小值。

# 实现效果

![效果图](http://storage1.imgchr.com/EXxRs.gif)