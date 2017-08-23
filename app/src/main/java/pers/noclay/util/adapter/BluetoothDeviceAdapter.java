package pers.noclay.util.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pers.noclay.util.R;

/**
 * Created by 82661 on 2016/9/24.
 */
public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    int resource;
    public BluetoothDeviceAdapter(Context context, int resource, List<BluetoothDevice> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice device = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.address = (TextView) view.findViewById(R.id.address);
            viewHolder.isPaired = (TextView) view.findViewById(R.id.isPaired);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(device.getName());
        viewHolder.address.setText(device.getAddress());
        if(device.getBondState() == BluetoothDevice.BOND_BONDED){
            viewHolder.isPaired.setVisibility(View.VISIBLE);
        }else{
            viewHolder.isPaired.setVisibility(View.INVISIBLE);
        }
        return view;
    }
    private class ViewHolder{
        TextView name;
        TextView address;
        TextView isPaired;
    }
}
