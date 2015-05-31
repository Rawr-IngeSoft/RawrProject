package com.example.david.rawr.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Tasks.GetPhoto;
import com.example.david.rawr.otherClasses.RoundImage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 31/05/2015.
 */
public class SearchFriendListAdapter  extends BaseAdapter{

    HashMap<String, Bitmap> friends;
    ArrayList<Pair<String, String>> searchedFriendList;
    Context context;
    private static LayoutInflater inflater;

    public SearchFriendListAdapter(Context context, HashMap<String, Bitmap> friends, ArrayList<Pair<String, String>> searchedFriendList) {
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.friends = friends;
        this.searchedFriendList = searchedFriendList;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int i) {
        return friends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder{
        TextView petUsername;
        ImageView petPicture;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_friend_row, null);
            holder.petUsername = (TextView) view.findViewById(R.id.search_friend_row_petUsername);
            holder.petPicture = (ImageView) view.findViewById(R.id.search_friend_row_petPicture);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Button sendRequest = (Button) view.findViewById(R.id.search_friend_sendRequest_button);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send request
            }
        });
        holder.petUsername.setText(searchedFriendList.get(i).first);
        holder.petPicture.setImageBitmap(RoundImage.getRoundedShape(friends.get(searchedFriendList.get(i).first)));
        return view;
    }
}
