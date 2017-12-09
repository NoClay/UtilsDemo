package pers.noclay.util.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;

import pers.noclay.util.R;
import pers.noclay.utiltool.BaseHandler;

/**
 * Created by NoClay on 2017/12/9.
 */

public class HandlerActivity extends AppCompatActivity {
    private TextView clock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_handler);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                        mHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initView() {
        clock = (TextView) findViewById(R.id.clock);
    }

    private BaseHandler mHandler = new BaseHandler<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0){
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                clock.setText(calendar.get(Calendar.HOUR_OF_DAY) + "："
                        + calendar.get(Calendar.MINUTE) + "："
                        + calendar.get(Calendar.SECOND));
            }
            return false;
        }
    });
}
