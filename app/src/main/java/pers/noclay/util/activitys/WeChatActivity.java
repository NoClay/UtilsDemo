package pers.noclay.util.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pers.noclay.bluetooth.Bluetooth;
import pers.noclay.bluetooth.BluetoothConfig;
import pers.noclay.util.R;
import pers.noclay.util.adapter.ListViewAdapter;
import pers.noclay.util.data.MessageForChat;

public class WeChatActivity extends AppCompatActivity {
    private TextView topButton;
    private ListView messageListView;
    private List<MessageForChat> messageList;
    private EditText editText;
    private Button sendButton;
    private ListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBluetooth();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initBluetooth() {
        BluetoothConfig config = new BluetoothConfig.Builder(this)
                .setAutoPairAble(true)
                .build();
        Bluetooth.initialize(config);
    }

    private void initView() {
        topButton = (TextView) findViewById(R.id.topButton);
        messageListView = (ListView) findViewById(R.id.msgListView);
        editText = (EditText) findViewById(R.id.editText);
        sendButton = (Button) findViewById(R.id.send);
        messageList = new ArrayList<>();
        adapter = new ListViewAdapter(WeChatActivity.this, R.layout.item_chat, messageList);
        messageListView.setAdapter(adapter);
    }
}
