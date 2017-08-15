package pers.noclay.util.activitys;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pers.noclay.foldlineview.FoldLineView;
import pers.noclay.util.R;
import pers.noclay.util.data.MeasureFoldLine;

public class DemoFoldLineView extends AppCompatActivity implements FoldLineView.onScrollChartListener{

    FoldLineView mFoldLineView;
    List<MeasureFoldLine> data;
    List<Integer> colors;
    private static final String TAG = "DemoFoldLineView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_fold_line_view);
        setupView();
        setupData();
        setupColor();
        mFoldLineView.setColors(colors);
        mFoldLineView.setLines(data);
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
