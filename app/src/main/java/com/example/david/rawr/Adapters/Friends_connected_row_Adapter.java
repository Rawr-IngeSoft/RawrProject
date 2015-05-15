package com.example.david.rawr.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.R;
import com.example.david.rawr.otherClasses.RoundImage;

import java.util.ArrayList;

/**
 * Created by Estudiante on 05/05/2015.
 */
public class Friends_connected_row_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Friend> friends;
    private static LayoutInflater inflater;

    public Friends_connected_row_Adapter(Context context, ArrayList<Friend> friends) {
        this.friends = friends;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.friends_connected_row, null);
            TextView petName = (TextView)convertView.findViewById(R.id.friends_connected_row_petName);
            ImageView picture = (ImageView)convertView.findViewById(R.id.friends_connected_row_picture);
            LinearLayout container = (LinearLayout)convertView.findViewById(R.id.friends_connected_row_container);
            petName.setText(friends.get(position).getPetName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profilepicture_male);
            picture.setImageBitmap(RoundImage.getRoundedShape(bitmap));
            if (friends.get(position).isConnected()){
                container.setBackgroundColor(Color.parseColor("#c6f274"));
            }else{
                container.setBackgroundColor(Color.parseColor("#c5e0d2"));
            }
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }
}
