package com.example.david.rawr.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.Fragments.ChoosePetFragment;
import com.example.david.rawr.Fragments.FriendRequest_fragment;
import com.example.david.rawr.Models.FriendRequest;
import com.example.david.rawr.Models.Pet;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by david on 21/05/2015.
 */
public class FriendsRequestAdapter extends FragmentPagerAdapter{

    Context context;
    ArrayList<FriendRequest> friendRequests;
    SQLiteHelper SQLiteHelper;
    String myUsername;

    public FriendsRequestAdapter(android.support.v4.app.FragmentManager fm, Context context){
        super(fm);
        SQLiteHelper = new SQLiteHelper(context);
        friendRequests = SQLiteHelper.getFriendRequests();
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        myUsername = sharedPreferences.getString("petUsername","");
    }


    @Override
    public Fragment getItem(int i) {
        FriendRequest_fragment friendRequest_fragment = new FriendRequest_fragment();
        Bundle data = new Bundle();
        FriendRequest friendRequest = friendRequests.get(i);
        data.putString("myUsername", myUsername);
        data.putString("petName", friendRequest.getPetName());
        data.putString("petType", friendRequest.getPetType());
        data.putString("petBirthday", friendRequest.getPetBirthday());
        data.putString("petPicture", friendRequest.getPetPicture());
        data.putString("petUsername", friendRequest.getPetUsername());
        data.putString("petRace", friendRequest.getPetRace());
        data.putString("petGender", friendRequest.getPetGender());
        data.putString("ownerName", friendRequest.getOwnerName());
        data.putString("ownerLastname", friendRequest.getOwnerLastName());
        data.putString("ownerPicture", friendRequest.getOwnerPicture());
        data.putString("ownerUsername", friendRequest.getOwnerUsername());
        friendRequest_fragment.setArguments(data);
        return friendRequest_fragment;
    }

    @Override
    public int getCount() {
        return friendRequests.size();
    }
}
