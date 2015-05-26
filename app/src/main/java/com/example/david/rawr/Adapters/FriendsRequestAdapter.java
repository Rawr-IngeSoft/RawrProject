package com.example.david.rawr.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.david.rawr.Models.FriendRequest;
import com.example.david.rawr.R;

import java.util.ArrayList;

/**
 * Created by david on 21/05/2015.
 */
public class FriendsRequestAdapter extends BaseAdapter {

    Context context;
    ArrayList<FriendRequest> friendRequests;
    private static LayoutInflater inflater;

    public FriendsRequestAdapter(ArrayList<FriendRequest> friendRequests, Context context) {
        this.friendRequests = friendRequests;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return friendRequests.size();
    }

    @Override
    public Object getItem(int position) {
        return friendRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
            convertView = inflater.inflate(R.friend_request_row, null);
            TextView sender = (TextView)convertView.findViewById(R.id.friend_request_sender);
            sender.setTypeface(type);
            sender.setText(friendRequests.get(position).getSender());
        }
        return convertView;
    }


}
