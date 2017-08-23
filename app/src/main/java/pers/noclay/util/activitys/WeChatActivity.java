package pers.noclay.util.activitys;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pers.noclay.bluetooth.Bluetooth;
import pers.noclay.bluetooth.BluetoothConfig;
import pers.noclay.bluetooth.BluetoothUtils;
import pers.noclay.bluetooth.OnFinishDiscoveryDevice;
import pers.noclay.bluetooth.OnPrepareBluetoothListener;
import pers.noclay.bluetooth.OnConnectListener;
import pers.noclay.util.R;
import pers.noclay.util.adapter.BluetoothDeviceAdapter;
import pers.noclay.util.adapter.ListViewAdapter;
import pers.noclay.util.data.MessageForChat;
import pers.noclay.util.dialog.SelectBluetoothDevice;

public class WeChatActivity extends AppCompatActivity implements View.OnClickListener, OnPrepareBluetoothListener, OnConnectListener{
    private TextView topButton;
    private ListView messageListView;
    private List<MessageForChat> messageList;
    private EditText editText;
    private Button sendButton;
    private ListViewAdapter adapter;
    private List<BluetoothDevice> devices;
    private SelectBluetoothDevice select;
    public static final String TARGET = "C4:0B:CB:79:D0:51";
    private static final String TAG = "WeChatActivity";
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_bluetooth);
        initView();
        initBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        Bluetooth.setOnConnectListener(this);
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
                showBluetoothSelect();
                break;
            }
            case R.id.send:{
                if (Bluetooth.isHasConnected()){
                    Bluetooth.sendMessage(editText.getText().toString());
                    MessageForChat chat = new MessageForChat(true, editText.getText().toString());
                    messageList.add(chat);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
                break;
            }
        }
    }

    private void showBluetoothSelect() {
        final ProgressDialog dialog = new ProgressDialog(WeChatActivity.this);
        dialog.setTitle("正在搜索附近的设备");
        dialog.show();

        Bluetooth.startSearch(new OnFinishDiscoveryDevice() {
            @Override
            public void onFinish(List<BluetoothDevice> deviceList) {
                dialog.dismiss();
                devices = deviceList;
                bluetoothDeviceAdapter = new BluetoothDeviceAdapter(WeChatActivity.this,
                        R.layout.item_show_bluetooth_device, devices);
                select = new SelectBluetoothDevice(WeChatActivity.this,
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                BluetoothDevice message = devices.get(i);
                                Toast.makeText(WeChatActivity.this,
                                        "正在连接" + message.getName() + " 地址：" + message.getAddress(),
                                        Toast.LENGTH_SHORT).show();
                                Bluetooth.setTargetAddress(message.getAddress());
                                Bluetooth.startConnect(WeChatActivity.this);
                            }
                        }, bluetoothDeviceAdapter);
                select.showAtLocation(findViewById(R.id.main_layout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
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

    @Override
    public void onConnectFail() {

    }

    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void onConnectStart() {

    }

    @Override
    public void onReceiveMessage(byte[] bytes) {
        Message message = Message.obtain();
        message.what = 0;
        message.obj = new String(bytes);
        mHandler.sendMessage(message);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                MessageForChat chat = new MessageForChat(false, (String) msg.obj);
                messageList.add(chat);
                adapter.notifyDataSetChanged();
            }
        }
    };
}
