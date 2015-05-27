package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.david.rawr.Adapters.Friends_connected_row_Adapter;
import com.example.david.rawr.Adapters.PostListAdapter;
import com.example.david.rawr.IRemoteService;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Interfaces.GetPostsResponse;
import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Services.Chat_service;
import com.example.david.rawr.Tasks.GetPhoto;
import com.example.david.rawr.Tasks.GetPosts;
import com.example.david.rawr.Models.Post;
import com.example.david.rawr.otherClasses.RoundImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class Newsfeed_screen extends Activity implements GetPostsResponse, View.OnClickListener, GetPhotoResponse {

    private ImageView localization, search, profile, notifications, profilePicture;
    private ListView postList;
    private SharedPreferences sharedPreferences;
    private String username = "";
    private Friends_connected_row_Adapter friends_connected_row_adapter;
    private PostListAdapter postListAdapter;
    private DrawerLayout dLayout;
    private ListView dList;
    private boolean buttonsVisible = true;
    private ArrayList<Friend> friendsList = new ArrayList<>();
    private ArrayList<Post> postsList = new ArrayList<>();
    private RelativeLayout buttons_container;
    private float  notificationsX, notificationsY,parentX, parentY, profileX, profileY, searchX, searchY, localizationX, localizationY;
    private Timer friendsConnectedTimer;
    private  NotificationManager notificationManager;
    // DB Manager
    SQLiteHelper  sqLiteHelper = new SQLiteHelper(this);

    // Background service connection declaration
    private ServiceConnection mConnection;
    protected IRemoteService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bitmap bitmap;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newsfeed_screen);

        localization = (ImageView)findViewById(R.id.newsfeed_imageView_localization);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_location);
        localization.setImageBitmap(bitmap);
        // TODO change click event
        localization.setOnClickListener(this);
        search = (ImageView)findViewById(R.id.newsfeed_imageView_search);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_search);
        search.setImageBitmap(bitmap);

        profile = (ImageView)findViewById(R.id.newsfeed_imageView_profile);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_profile);
        profile.setImageBitmap(bitmap);
        profile.setOnClickListener(this);

        notifications = (ImageView)findViewById(R.id.newsfeed_imageView_notifications);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_notifications);
        notifications.setImageBitmap(bitmap);

        postList = (ListView)findViewById(R.id.newsfeed_list);
        profilePicture = (ImageView)findViewById(R.id.newsfeed_imageView_profilePicture);
        buttons_container = (RelativeLayout)findViewById(R.id.newsfeed_buttons_container);
        profilePicture.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Notification manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Connected friend list
        dList = (ListView) findViewById(R.id.newsfeed_friends_list);
        dList.setSelector(android.R.color.holo_blue_dark);

        // Getting friends
        friendsList = sqLiteHelper.getFriends();

        // Show postList
        postListAdapter = new PostListAdapter(this,postsList);
        postList.setAdapter(postListAdapter);

        // Getting posts
        if(sharedPreferences.contains("petUsername")) {
            Log.e("petUsername", sharedPreferences.getString("petUsername", ""));
            GetPosts getPosts = new GetPosts(sharedPreferences.getString("petUsername", ""), this);
            getPosts.execute();

        }

        //Show friendlist

        if(sharedPreferences.contains("petUsername"))
            friendsList.add(new Friend(sharedPreferences.getString("petUsername", ""), sharedPreferences.getString("petName", "")));
        friends_connected_row_adapter = new Friends_connected_row_Adapter(this, friendsList);
        dList.setAdapter(friends_connected_row_adapter);



        // Persistence services
        if(sharedPreferences.contains("pictureUri") && sharedPreferences.contains("petUsername")){
            GetPhoto getPhoto = new GetPhoto(sharedPreferences.getString("pictureUri", ""), sharedPreferences.getString("petusername",""), this);
            getPhoto.execute();
        }else{
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_profilepicture_male);
            profilePicture.setImageBitmap(RoundImage.getRoundedShape(largeIcon));
        }
        if(sharedPreferences.contains("username")) {
            username = sharedPreferences.getString("username", "");sharedPreferences.getString("username", "");
        }

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = IRemoteService.Stub.asInterface(binder);
                try {
                    ArrayList<String> connectedFriends = (ArrayList<String>)service.getFriendsList();
                    boolean connected = false;
                    if (connectedFriends != null) {
                        for(Friend friend: friendsList){
                            for (String connFriend: connectedFriends){
                                if (connFriend.equals(friend.getPetUsername())){
                                    friend.setConnected(true);
                                    connected = true;
                                }
                            }
                            if (!connected){
                                friend.setConnected(false);
                            }
                            connected = false;
                        }
                        Collections.sort(friendsList);
                        friends_connected_row_adapter.setFriends(friendsList);
                        dList.setAdapter(friends_connected_row_adapter);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (friendsList != null) {
                    friends_connected_row_adapter = new Friends_connected_row_Adapter(Newsfeed_screen.this, friendsList);
                    dList.setAdapter(friends_connected_row_adapter);
                }else{
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
            }
        };

        dLayout = (DrawerLayout) findViewById(R.id.newsfeed_friends_sliding_list);

        TextView friendsHeader = new TextView(this);
        friendsHeader.setText("Friends");
        friendsHeader.setTextColor(Color.parseColor("#000000"));
        friendsHeader.setBackgroundColor(Color.parseColor("#F0F0F0"));
        friendsHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        friendsHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        friendsHeader.setHeight(50);
        dList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

                dLayout.closeDrawers();
                Intent intent = new Intent(Newsfeed_screen.this, Chat_window.class);
                Friend f = (Friend)dList.getItemAtPosition(position);
                sharedPreferences.edit().putString("receiver", f.getPetUsername()).commit();
                startActivity(intent);
            }

        });

        // Animation of buttons variables and events
        notificationsX = notifications.getX();
        notificationsY = notifications.getY();
        profileX = profile.getX();
        profileY = profile.getY();
        searchX = search.getX();
        searchY = search.getY();
        localizationX = localization.getX();
        localizationY = localization.getY();
        parentX = profilePicture.getX();
        parentY = profilePicture.getY();
        profilePicture.bringToFront(); // Bring profile picture to front
        buttons_container.bringToFront(); //  bring container to front

        // bind connected friends service
        if (service == null) {
            Intent connected_friends_intent = new Intent();
            connected_friends_intent.setAction("service.Chat");
            ActivityManager am = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
            // Return a list of the tasks that are currently running,
            // with the most recent being first and older ones after in order.
            // Taken 1 inside getRunningTasks method means want to take only
            // top activity from stack and forgot the olders.
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

            ComponentName componentInfo = taskInfo.get(0).topActivity;
            componentInfo.getPackageName();
            connected_friends_intent.setPackage(componentInfo.getPackageName());
            this.bindService(connected_friends_intent, mConnection, BIND_AUTO_CREATE);
        }

        // Refresh friend list
        friendsConnectedTimer = new Timer();
        friendsConnectedTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Newsfeed_screen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(service != null) {
                                boolean connected = false;
                                ArrayList<String> connectedFriends = (ArrayList<String>) service.getFriendsList();
                                if (connectedFriends != null) {
                                    for(Friend friend: friendsList){
                                        for (String connFriend: connectedFriends){
                                            if (connFriend.equals(friend.getPetUsername())){
                                                friend.setConnected(true);
                                                connected = true;
                                            }
                                        }
                                        if (!connected){
                                            friend.setConnected(false);
                                        }
                                        connected = false;
                                    }
                                    Collections.sort(friendsList);
                                    friends_connected_row_adapter.setFriends(friendsList);
                                    dList.setAdapter(friends_connected_row_adapter);
                                }
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 2000, 2000);
    }

    @Override
    public void getPostsFinish(ArrayList<Post> output) {
        Log.e("posts","");
        postsList = output;
        for(Post p: postsList){
            Log.e(p.getPetUsername(), p.getText());
        }
        postListAdapter.setData(postsList);
        postListAdapter.notifyDataSetChanged();
        postList.setAdapter(postListAdapter);

    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch(v.getId()){
            case R.id.newsfeed_imageView_localization:
                intent = new Intent(this, Friend_requests_screen.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.newsfeed_imageView_profile:
                intent = new Intent(this, Owner_Profile_screen.class);
                startActivity(intent);
                this.finish();
                break;

            case R.id.newsfeed_imageView_profilePicture:
                TranslateAnimation animation_notification, animation_profile, animation_search, animation_localization;
                if (buttonsVisible == true){
                    buttonsVisible = false;
                    animation_profile = new TranslateAnimation(0, -parentX+notificationsX-90, 0, -parentY+notificationsY-30);
                    animation_search = new TranslateAnimation(0, -parentX+profileX-90, 0, -parentY+profileY+20);
                    animation_notification = new TranslateAnimation(0, -parentX+searchX-40, 0, -parentY+searchY-80);
                    animation_localization = new TranslateAnimation(0, -parentX+localizationX+40, 0, -parentY+localizationY-90);
                }else {
                    animation_profile = new TranslateAnimation( 0, parentX-notificationsX+90, 0,  parentY-notificationsY+30);
                    animation_search = new TranslateAnimation( 0, parentX-profileX+90, 0,  parentY-profileY-20);
                    animation_notification = new TranslateAnimation( 0, parentX-searchX+40, 0,  parentY-searchY+80);
                    animation_localization = new TranslateAnimation(0, parentX-localizationX-40, 0, parentY-localizationY+90);
                    buttonsVisible = true;
                }
                animation_notification.setDuration(500);
                animation_profile.setDuration(500);
                animation_search.setDuration(500);
                animation_localization.setDuration(500);

                animation_localization.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        localization.clearAnimation();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) localization.getLayoutParams();
                        if (buttonsVisible == false) {
                            params.topMargin += (-parentY + localizationY-90);
                            params.leftMargin += (-parentX + localizationX+40);
                        } else {
                            params.topMargin += (parentY - localizationY+90);
                            params.leftMargin += (parentX - localizationX-40);
                        }
                        localization.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animation_notification.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        notifications.clearAnimation();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) notifications.getLayoutParams();
                        if (buttonsVisible == false) {
                            params.topMargin += (-parentY + searchY-80);
                            params.leftMargin += (-parentX + searchX - 40);
                        } else {
                            params.topMargin += (parentY - searchY+80);
                            params.leftMargin += (parentX - searchX + 40);
                        }
                        notifications.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animation_search.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        search.clearAnimation();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) search.getLayoutParams();
                        if (buttonsVisible == false) {
                            params.topMargin += (-parentY + profileY+20);
                            params.leftMargin += (-parentX + profileX - 90);
                        } else {
                            params.topMargin += (parentY - profileY-20);
                            params.leftMargin += (parentX - profileX + 90);
                        }
                        search.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animation_profile.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        profile.clearAnimation();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) profile.getLayoutParams();
                        if (buttonsVisible == false) {
                            params.topMargin += (-parentY + notificationsY - 30);
                            params.leftMargin += (-parentX + notificationsX - 90);
                        } else {
                            params.topMargin += (parentY - notificationsY + 30);
                            params.leftMargin += (parentX - notificationsX + 90);
                        }
                        profile.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                notifications.startAnimation(animation_notification);
                profile.startAnimation(animation_profile);
                search.startAnimation(animation_search);
                localization.startAnimation(animation_localization);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        friendsConnectedTimer.cancel();
        unbindService(mConnection);
    }


    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        if(bitmap!= null){
            profilePicture.setImageBitmap(RoundImage.getRoundedShape(bitmap));
        }else{
            profilePicture.setImageBitmap(RoundImage.getRoundedShape(BitmapFactory.decodeResource(getResources(), R.drawable.default_profilepicture_male)));
        }
    }
}
