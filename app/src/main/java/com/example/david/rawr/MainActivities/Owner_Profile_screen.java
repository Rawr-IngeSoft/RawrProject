package com.example.david.rawr.MainActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.david.rawr.Adapters.PetChooseViewPagerAdapter;
import com.example.david.rawr.Interfaces.GetFriendsResponse;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.Models.Pet;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Tasks.GetFriends;
import com.example.david.rawr.Tasks.GetPhoto;

import java.util.ArrayList;


// REQ-029
public class Owner_Profile_screen extends FragmentActivity implements GetPhotoResponse, View.OnLongClickListener,GetFriendsResponse{

    ImageView photo;
    TextView birthdayText, nameText, addressText, lastNameText;
    SharedPreferences sharedpreferences;
    ViewPager petProfile;
    LinearLayout ownerLayout;
    PetChooseViewPagerAdapter petChooseViewPagerAdapter;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__profile_screen);
        ownerLayout = (LinearLayout)findViewById(R.id.owner_profile_owner_layout);
        ownerLayout.setOnLongClickListener(this);
        photo = (ImageView)findViewById(R.id.picture);
        birthdayText = (TextView)findViewById(R.id.owner_profile_birthday);
        nameText = (TextView)findViewById(R.id.owner_profile_name);
        lastNameText = (TextView)findViewById(R.id.owner_profile_lastName);
        addressText = (TextView)findViewById(R.id.owner_profile_address);
        petProfile = (ViewPager)findViewById(R.id.owner_profile_viewPager);
        fm = getSupportFragmentManager();
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        petChooseViewPagerAdapter = new PetChooseViewPagerAdapter(fm,this);
        petProfile.setAdapter(petChooseViewPagerAdapter);
        if(sharedpreferences.contains("name")) {
            nameText.setText(sharedpreferences.getString("name", "") + " " );
        }
        if(sharedpreferences.contains("lastName")){
            lastNameText.setText(sharedpreferences.getString("lastName", ""));
        }
        if(sharedpreferences.contains("ownerPictureUri") && sharedpreferences.contains("petUsername")) {
            GetPhoto getPhoto = new GetPhoto(sharedpreferences.getString("ownerPictureUri", ""),sharedpreferences.getString("petUsername", ""), this);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {

    }

    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){
            case R.id.owner_profile_owner_layout:
                Intent intent = new Intent(this, Configuration_screen.class);
                this.finish();
                startActivity(intent);
            break;
        }
        return false;
    }

    public void update(String petUsername){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("petUsername", petUsername);
        editor.commit();

        // Gettings friends of new pet
        GetFriends getFriends = new GetFriends(petUsername, this);
        getFriends.execute();

        // TODO reload all data with selected pet

    }

    @Override
    public void getFriendsFinish(ArrayList<Friend> output) {

        // Cleaning local db and getting all data of new pet
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.clearFriends();
        for(Friend friend: output){
            sqLiteHelper.addFriend(friend);
        }
        this.finish();
    }
}
