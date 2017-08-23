package pers.noclay.util.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pers.noclay.util.R;
import pers.noclay.util.data.Demo;

/**
 * Created by i-gaolonghai on 2017/8/9.
 */

public class DemoListAdapter extends ArrayAdapter<Demo> {
    private List<Demo> demoList;
    private Context context;
    private int resource;
    private static final String TAG = "DemoListAdapter";
    public DemoListAdapter(@NonNull Context context, int resource, List<Demo> demoList) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.demoList = demoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            holder.textView = view.findViewById(R.id.text);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        Demo demo = demoList.get(position);
        holder.textView.setText(demo.getTitle());
        return view;
    }

    @Override
    public int getCount() {
        return demoList.size();
    }

    class ViewHolder{
        TextView textView;
    }
}
