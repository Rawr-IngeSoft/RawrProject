package com.example.david.rawr.MainActivities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.Adapters.PetChooseViewPagerAdapter;
import com.example.david.rawr.IRemoteService;
import com.example.david.rawr.Interfaces.GetFriendsResponse;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.Models.Pet;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Tasks.GetFriends;
import com.example.david.rawr.Tasks.GetPhoto;
import com.example.david.rawr.otherClasses.RoundImage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;


// REQ-029
public class Owner_Profile_screen extends FragmentActivity implements GetPhotoResponse, View.OnLongClickListener,GetFriendsResponse{

    ImageView photo;
    TextView birthdayText, nameText, addressText, lastNameText;
    SharedPreferences sharedpreferences;
    ViewPager petProfile;
    LinearLayout ownerLayout;
    PetChooseViewPagerAdapter petChooseViewPagerAdapter;
    FragmentManager fm;
    // Background service connection declaration
    private ServiceConnection mConnection;
    protected IRemoteService service;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // service bind
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = IRemoteService.Stub.asInterface(binder);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
            }
        };
        if (service == null) {
            Intent connected_friends_intent = new Intent();
            connected_friends_intent.setAction("service.Chat");
            this.bindService(connected_friends_intent, mConnection, BIND_AUTO_CREATE);
            Log.e("status", "bind");
        }
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
        Intent intent = new Intent(this, Newsfeed_screen.class);
        startActivity(intent);
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

        // Updating service
        try {
            service.changePet(petUsername);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Updating db and shared preferences
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.selectPet(petUsername,sharedpreferences);
        // Gettings friends of new pet
        GetFriends getFriends = new GetFriends(petUsername, this);
        getFriends.execute();

    }

    public void refreshCreatedPetPicture(Uri targetUri, ImageView petPicture){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("pictureUri", targetUri.toString());
        editor.commit();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
            petPicture.setImageBitmap(RoundImage.getRoundedShape(bitmap));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Error loading the photo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void getFriendsFinish(ArrayList<Friend> output) {

        // Cleaning local db and getting all data of new pet
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.clearFriends();
        for(Friend friend: output){
            sqLiteHelper.addFriend(friend);
        }
        Intent intent = new Intent(this, Newsfeed_screen.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
