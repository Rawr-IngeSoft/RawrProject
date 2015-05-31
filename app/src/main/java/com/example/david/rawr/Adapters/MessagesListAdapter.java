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
public class MessagesListAdapter extends BaseAdapter implements GetPhotoResponse {
    Context context;
    ArrayList<Message> data;
    private static LayoutInflater inflater;
    private String username;

    public MessagesListAdapter(Context context, ArrayList<Message> data, String username) {
        this.context = context;
        this.data = data;
        this.username = username;
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
    public void getPhotoFinish(Bitmap bitmap) {

    }

    static class ViewHolder{
        TextView msgTVMe, dateTVMe;
        ImageView msgOwnerPictureMe;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.messages_list_row_receiver, null);
            holder.msgTVMe = (TextView) convertView.findViewById(R.id.message_text);
            holder.msgOwnerPictureMe = (ImageView) convertView.findViewById(R.id.message_postOwner_picture);
            holder.dateTVMe = (TextView) convertView.findViewById(R.id.messages_list_row_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }/*
        try {
            holderMe.msgOwnerPicture.setImageBitmap(new GetPhoto(data.get(position).getPictureUri(), data.get(position).getSender(), this).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        holder.msgTVMe.setText(data.get(position).getMessage());
        holder.dateTVMe.setText(data.get(position).getDate());
        holder.msgOwnerPictureMe.setBackgroundColor(Color.parseColor("#000000"));
        if (!data.get(position).isVisible()) {
            holder.msgOwnerPictureMe.setVisibility(View.INVISIBLE);
        } else {
            holder.msgOwnerPictureMe.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public ArrayList<Message> getData() {
        return data;
    }

    public void setData(ArrayList<Message> data) {
        this.data = data;
    }
}
