package pers.noclay.util.activitys;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pers.noclay.bluetooth.Bluetooth;
import pers.noclay.bluetooth.BluetoothConfig;
import pers.noclay.bluetooth.BluetoothUtils;
import pers.noclay.bluetooth.OnFinishDiscoveryDevice;
import pers.noclay.bluetooth.OnPrepareBluetoothListener;
import pers.noclay.bluetooth.OnStartConnectListener;
import pers.noclay.util.R;
import pers.noclay.util.adapter.ListViewAdapter;
import pers.noclay.util.data.MessageForChat;

public class WeChatActivity extends AppCompatActivity implements View.OnClickListener, OnPrepareBluetoothListener {
    private TextView topButton;
    private ListView messageListView;
    private List<MessageForChat> messageList;
    private EditText editText;
    private Button sendButton;
    private ListViewAdapter adapter;
    public static final String TARGET = "C4:0B:CB:79:D0:51";
    private static final String TAG = "WeChatActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_bluetooth);
        initView();
        initBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bluetooth.onDestroy();
    }

    private void initBluetooth() {
        BluetoothConfig config = new BluetoothConfig.Builder(this)
                .setAutoPairAble(true)
                .setTargetAddress(TARGET)
                .build();
        Bluetooth.initialize(config);
        Bluetooth.setPrepareBluetoothListener(this);
    }

    private void initView() {
        topButton = (TextView) findViewById(R.id.topButton);
        messageListView = (ListView) findViewById(R.id.msgListView);
        editText = (EditText) findViewById(R.id.editText);
        sendButton = (Button) findViewById(R.id.send);
        messageList = new ArrayList<>();
        adapter = new ListViewAdapter(WeChatActivity.this, R.layout.item_chat, messageList);
        messageListView.setAdapter(adapter);
        sendButton.setOnClickListener(this);
        topButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.topButton:{
                Bluetooth.setTargetAddress(TARGET);
                Log.d(TAG, "onClick: 开始连接 target = " + Bluetooth.getTargetAddress());
                Log.d(TAG, "onClick: " + BluetoothUtils.getMacAddress(TARGET));
                Bluetooth.startConnect(new OnStartConnectListener() {
                    @Override
                    public void onConnectFail() {

                    }

                    @Override
                    public void onConnectSuccess() {

                    }
                });
                break;
            }
            case R.id.send:{
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bluetooth.onResult(requestCode, resultCode, data);
    }

    @Override
    public void onNonSupportable(String tips) {
        Log.d(TAG, "onNonSupportable: " + tips);
    }

    @Override
    public void onOpenBluetooth(boolean success) {
        Log.d(TAG, "onOpenBluetooth: success = " + success);
    }

    @Override
    public void onRequestPermission(boolean success, String[] permissions) {
        Log.d(TAG, "onRequestPermission: success = " + success);
        Log.d(TAG, "onRequestPermission: permissions = " + Arrays.toString(permissions));
    }

    @Override
    public void onOpenBluetoothDiscoverable() {
        Log.d(TAG, "onOpenBluetoothDiscoverable: ");
    }

    @Override
    public void onCloseBluetoothDiscoverable() {
        Log.d(TAG, "onCloseBluetoothDiscoverable: ");
    }
}
