package com.example.david.rawr.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Tasks.GetPhoto;

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
        holder.petUsername.setText(searchedFriendList.get(i).first);
        holder.petPicture.setImageBitmap(friends.get(searchedFriendList.get(i).first));
        return null;
    }
}
