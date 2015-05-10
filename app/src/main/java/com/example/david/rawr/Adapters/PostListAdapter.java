package com.example.david.rawr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.david.rawr.R;
import com.example.david.rawr.Models.Post;

import java.util.ArrayList;

/**
 * Created by david on 24/04/2015.
 */
public class PostListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Post> data;
    private static LayoutInflater inflater;

    public PostListAdapter(Context context, ArrayList<Post> data) {
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
            convertView = inflater.inflate(R.layout.post_row, null);
            TextView petIdTv = (TextView)convertView.findViewById(R.id.post_petId);
            TextView dateTv = (TextView)convertView.findViewById(R.id.post_date);
            TextView textTv = (TextView)convertView.findViewById(R.id.post_text);
            petIdTv.setText(data.get(position).getPetUsername());
            dateTv.setText(data.get(position).getDate());
            textTv.setText(data.get(position).getText());
        }
        return convertView;
    }
}
