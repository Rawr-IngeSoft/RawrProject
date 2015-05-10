package com.example.david.rawr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.david.rawr.R;
import com.example.david.rawr.models.Post;

import java.util.ArrayList;

/**
 * Created by david on 09/05/2015.
 */
public class MessagesListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater;

    public MessagesListAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.messages_list_row, null);
            TextView msgTV = (TextView)convertView.findViewById(R.id.messages_list_row_message);
            msgTV.setText(data.get(position));
        }
        return convertView;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}