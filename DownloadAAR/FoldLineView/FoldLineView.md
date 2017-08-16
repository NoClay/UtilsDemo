# FoldLineView

这是一个折线图的自定义View，用户可以自定义添加线的条数，具体使用方法如下。

# 属性

自定义属性如下:

```xml
    <declare-styleable name="FoldLineView">
        <attr name="backLineColor" format="color"/>
        背景线的颜色
        <attr name="lineColor" format="color"/>
        折线的颜色，可以不设置，通过Java代码可以设置每条线的颜色
        <attr name="labelColor" format="color"/>
        标签的颜色，即x，y轴的颜色
        <attr name="labelSize" format="dimension"/>
        标签的字体的大小
        <attr name="xScaleNum" format="integer"/>
        x轴的格子的个数，默认为6
        <attr name="yScaleNum" format="integer"/>
        y轴的格子的个数，默认为9
        <attr name="yStart" format="float"/>
        y轴的范围，为低谷值
        <attr name="yEnd" format="float"/>
        y轴的范围，为高峰值
    </declare-styleable>
```

# 方法

##普通方法

以上属性均可以通过set，get方法进行获取，此处不再列举

```java
    public List<Integer> getColors() 
    public void setColors(List<Integer> colors)  
    public void startDrawing() 
    public void stopDrawing() 
```

**控制是否刷新视图，注意在方法调用时，start只能启动没有开始的视图，建议在onResume方法中调用startDrawing，在onPause中调用stopDrawing**

## 如何定义对应的数据格式

1. 通过接口实现对应的方法

   ```java
   public class Data implements FoldLineInterface
   public interface FoldLineInterface {
       List<Float> getLinesAsList();
       获取对应折线上的点，有多少个点，就代表需要有多少条折线
       即获取当x轴为某一个值的时候，y轴的数据情况
       String getLabel();
       获取x轴的标签
   }
   ```

2. 通过继承抽象类实现

   ```java
   public class Data extends FoldLineBean
   public abstract class FoldLineBean implements FoldLineInterface{
       @Override
       public List<Float> getLinesAsList() {
           return null;
       }

       @Override
       public String getLabel() {
           return null;
       }
   }
   ```

## 如何传入数据

利用FoldLineAdapter传入，无论是实现接口还是继承，均通过该方法设置

```java

public class DemoFoldLineView extends AppCompatActivity implements FoldLineView.onScrollChartListener{

    FoldLineView mFoldLineView;
    List<MeasureFoldLine> data;
    List<Integer> colors;
    FoldLineAdapter<MeasureFoldLine> mAdapter;
    private static final String TAG = "DemoFoldLineView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_fold_line_view);
        setupView();
        setupData();
        setupColor();
        mAdapter = new FoldLineAdapter<>(data);
        mFoldLineView.setColors(colors);
        mFoldLineView.setAdapter(mAdapter);
        mFoldLineView.setOnScrollChartListener(this);
        mFoldLineView.startDrawing();
    }

    private void setupColor() {
        if (colors == null){
            colors = new ArrayList<>();
        }
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
    }

    private void setupData() {
        if (data == null){
            data = new ArrayList<>();
        }else{
            data.clear();
        }
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            MeasureFoldLine m = new MeasureFoldLine(
                    (float)(random.nextInt(600) - 300),
                    (float)(random.nextInt(600) - 300),
                    (float)(random.nextInt(600) - 300),
                    i +"");
            data.add(m);
        }
    }

    private void setupView() {
        mFoldLineView = (FoldLineView) findViewById(R.id.foldLine);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFoldLineView.startDrawing();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFoldLineView.stopDrawing();
    }

    @Override
    public void onScroll(int left, int right) {
        Log.d(TAG, "onScroll: left = " + left);
        Log.d(TAG, "onScroll: right = " + right);
    }
}
```

# 接口定义

```java
    public interface OnScrollChartListener {
        void onScroll(int left, int right);
    }
```

当滑动的时候，会默认的调用该接口，返回滑动时窗口的左边序号和右边序号

**注意：实现该接口的时候，默认接口是在子线程调用的，请利用Handler和Message机制切换到UI线程。**

# 实现效果

![效果图](http://storage1.imgchr.com/Vuf2R.gif)