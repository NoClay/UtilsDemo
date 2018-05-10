package pers.noclay.util.activitys;

import android.Manifest;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import pers.noclay.util.R;
import pers.noclay.utiltool.BaseHandler;
import pers.noclay.utiltool.BitmapUtils;
import pers.noclay.utiltool.SystemUtils;

import static pers.noclay.utiltool.ShareUtils.shareText;
import static pers.noclay.utiltool.ShareUtils.shareTextToQQ;


/**
 * Created by NoClay on 2017/12/9.
 */

public class HandlerActivity extends AppCompatActivity implements OnClickListener {
    private TextView clock;
    private Button share;
    private Button shareToWeChat;
    private Button shareToQQ;
    private ImageView test;
    private LinearLayout layer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_handler);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
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
        share = (Button) findViewById(R.id.share);
        shareToWeChat = (Button) findViewById(R.id.shareToWeChat);
        shareToQQ = (Button) findViewById(R.id.shareToQQ);
        share.setOnClickListener(this);
        shareToWeChat.setOnClickListener(this);
        shareToQQ.setOnClickListener(this);
        test = (ImageView) findViewById(R.id.test);
        layer = (LinearLayout) findViewById(R.id.layer);
    }

    private BaseHandler mHandler = new BaseHandler<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                clock.setText(calendar.get(Calendar.HOUR_OF_DAY) + "："
                        + calendar.get(Calendar.MINUTE) + "："
                        + calendar.get(Calendar.SECOND));
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share: {
                shareText(this, "分享的标题", "分享内容");
                break;
            }
            case R.id.shareToQQ: {
                shareTextToQQ(this, "分享的标题", "分享内容");
                break;
            }
            case R.id.shareToWeChat: {
//                shareTextToWeChat(this, "分享的标题", "分享内容");
                test.setImageDrawable(new BitmapDrawable(BitmapUtils.getBitmapByView(layer)));
                SystemUtils.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/"
                        + System.currentTimeMillis() + ".png";
                Toast.makeText(this, "path = " + path, Toast.LENGTH_SHORT).show();
                BitmapUtils.saveBitmapToFile(BitmapUtils.getBitmapByView(layer), path, 99);
                break;
            }
        }
    }
}
