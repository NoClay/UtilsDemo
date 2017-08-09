package pers.noclay.util.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

import pers.noclay.ecgwaveview.ECGWaveView;
import pers.noclay.util.R;


public class DemoECGWavesView extends AppCompatActivity implements View.OnClickListener{
    ECGWaveView wavesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_ecgwaves_view);
        setupViews();
        setupData();

    }

    private void setupData() {
        final Random random = new Random();
        new Thread(new Runnable() {
            int count = 0;
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(3);
                        if (count < 133){
                            wavesView.drawNextPoint(random.nextInt(30) + 2000);
                        }else if (count < 166){
                            wavesView.drawNextPoint(2000 + 54 * (count - 133));
                        }else if (count < 232){
                            wavesView.drawNextPoint(3800 - 54 * (count - 166));
                        }else if (count < 265){
                            wavesView.drawNextPoint(200 + 54 * (count - 232));
                        }else if (count < 298){
                            wavesView.drawNextPoint(random.nextInt(30) + 2000);
                        }else if (count < 331){
                            wavesView.drawNextPoint(2000 + 54 * (count - 331));
                        }else if (count < 397){
                            wavesView.drawNextPoint(3800 - 54 * (count - 397));
                        }else if (count < 430){
                            wavesView.drawNextPoint(200 + 54 * (count - 430));
                        }else if (count < 463){
                            wavesView.drawNextPoint(random.nextInt(30) + 2000);
                        }else {
                            count = 0;
                        }
                        count ++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void setupViews() {
        wavesView = (ECGWaveView) findViewById(R.id.heartWaves);
        findViewById(R.id.startBt).setOnClickListener(this);
        findViewById(R.id.stopBt).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startBt:{
                wavesView.startRefresh();
                break;
            }
            case R.id.stopBt:{
                wavesView.stopRefresh();
                break;
            }
        }
    }
}
