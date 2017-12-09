package pers.noclay.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pers.noclay.util.activitys.DemoECGWavesView;
import pers.noclay.util.activitys.DemoFoldLineView;
import pers.noclay.util.activitys.HandlerActivity;
import pers.noclay.util.activitys.WeChatActivity;
import pers.noclay.util.adapter.DemoListAdapter;
import pers.noclay.util.data.Demo;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private List<Demo> demos;
    private ListView listView;
    private DemoListAdapter adapter;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        addDemo("显示心电图的demo", DemoECGWavesView.class);
        addDemo("测试折线图", DemoFoldLineView.class);
        addDemo("测试蓝牙SDK", WeChatActivity.class);
        addDemo("测试Handler", HandlerActivity.class);
    }

    private void addDemo(String title, Class<? extends Activity> activity) {
        if (demos == null){
            demos = new ArrayList<>();
        }
        demos.add(new Demo(title, new Intent(this, activity)));
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.demoListView);
        adapter = new DemoListAdapter(this, R.layout.item_demo, demos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        mContext = this;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Demo demo = demos.get(i);
        if (demo != null){
            mContext.startActivity(demo.getIntent());
        }
    }
}
