package pers.noclay.util.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import pers.noclay.util.R;
import pers.noclay.util.adapter.BluetoothDeviceAdapter;

/**
 * Created by 82661 on 2016/9/24.
 */
public class SelectBluetoothDevice extends PopupWindow {
    private TextView cancel;
    private ListView showBluetoothDevice;
    private View view;
    public SelectBluetoothDevice(Context context , AdapterView.OnItemClickListener listener,
                                 BluetoothDeviceAdapter adapter) {
        super(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.dialog_select_bluetooth, null);

        cancel = (TextView) view.findViewById(R.id.cancel_button);
        showBluetoothDevice = (ListView) view.findViewById(R.id.show_bluetooth_device);
        showBluetoothDevice.setAdapter(adapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        showBluetoothDevice.setOnItemClickListener(listener);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
//        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_window_background));
//        this.getBackground().setAlpha(200);
        ColorDrawable dw = new ColorDrawable(0x88000000);
        this.setBackgroundDrawable(dw);


    }
}
