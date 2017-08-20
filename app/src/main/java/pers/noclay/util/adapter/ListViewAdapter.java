package pers.noclay.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pers.noclay.util.R;
import pers.noclay.util.data.MessageForChat;

/**
 * Created by 82661 on 2016/9/24.
 */
public class ListViewAdapter extends ArrayAdapter<MessageForChat> {
    int resource;

    public ListViewAdapter(Context context, int resource, List<MessageForChat> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageForChat messageForChat = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_chat, null);
            viewHolder = new ViewHolder();
            viewHolder.leftText = (TextView) view.findViewById(R.id.left_text);
            viewHolder.rightText = (TextView) view.findViewById(R.id.right_text);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (messageForChat.isAuthor()){
            viewHolder.leftText.setVisibility(View.INVISIBLE);
            viewHolder.rightText.setText(messageForChat.getContent());
        }else{
            viewHolder.rightText.setVisibility(View.INVISIBLE);
            viewHolder.leftText.setText(messageForChat.getContent());
        }
        return view;
    }
    private class ViewHolder{
        TextView leftText;
        TextView rightText;
    }
}
