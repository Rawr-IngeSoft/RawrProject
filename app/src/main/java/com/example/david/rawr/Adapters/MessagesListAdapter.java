package com.example.david.rawr.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.Tasks.GetPhoto;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by david on 09/05/2015.
 */
public class MessagesListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Message> data;
    private static LayoutInflater inflater;
    Bitmap senderBitmap, receiverBitmap;
    private String username;

    public MessagesListAdapter(Context context, ArrayList<Message> data, String username, Bitmap senderBitmap, Bitmap receiverBitmap) {
        this.context = context;
        this.data = data;
        this.username = username;
        this.senderBitmap = senderBitmap;
        this.receiverBitmap = receiverBitmap;
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

    static class ViewHolder{
        TextView msgTVMe, dateTVMe;
        ImageView msgOwnerPictureMe;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            if (viewType == 1) {
                convertView = inflater.inflate(R.layout.messages_list_row_sender, null);
            }else {
                convertView = inflater.inflate(R.layout.messages_list_row_receiver, null);
            }
            holder.msgTVMe = (TextView) convertView.findViewById(R.id.message_text);
            holder.msgOwnerPictureMe = (ImageView) convertView.findViewById(R.id.message_picture);
            holder.dateTVMe = (TextView) convertView.findViewById(R.id.messages_list_row_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.msgTVMe.setText(data.get(position).getMessage());
        holder.dateTVMe.setText(data.get(position).getDate());
        if (viewType == 1){
            holder.msgOwnerPictureMe.setImageBitmap(senderBitmap);
        }else{
            holder.msgOwnerPictureMe.setImageBitmap(receiverBitmap);
        }
        if (data.get(position).isVisible()) {
            holder.msgOwnerPictureMe.setVisibility(View.VISIBLE);
        } else {
            holder.msgOwnerPictureMe.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public ArrayList<Message> getData() {
        return data;
    }

    public void setData(ArrayList<Message> data) {
        this.data = data;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (data.get(position).getSender().compareTo(username) == 0) ? 0 : 1;
    }
}
