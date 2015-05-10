package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
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
import com.example.david.rawr.Interfaces.GetPostsResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Services.Chat_service;
import com.example.david.rawr.Tasks.GetPhoto;
import com.example.david.rawr.Tasks.GetPosts;
import com.example.david.rawr.models.Post;
import com.example.david.rawr.otherClasses.RoundImage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class Newsfeed_screen extends Activity implements GetPostsResponse, View.OnClickListener {

    ImageView localization, messages, profile, notifications, profilePicture;
    ListView postList;
    SharedPreferences sharedPreferences;
    String username = "";
    Friends_connected_row_Adapter friends_connected_row_adapter;
    DrawerLayout dLayout;
    ListView dList;
    boolean buttonsVisible = true;
    ArrayList<String> friendsList = new ArrayList<>();
    RelativeLayout buttons_container;
    float  notificationsX, notificationsY,parentX, parentY, profileX, profileY, messagesX, messagesY, localizationX, localizationY;
    Timer friendsConnectedTimer;
    Intent connected_friends_intent;
    NotificationManager notificationManager;
    // Background service class declaration

    Chat_service chat_service;

    // Background service connection declaration
    private ServiceConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newsfeed_screen);
        localization = (ImageView)findViewById(R.id.newsfeed_localization_button);
        messages = (ImageView)findViewById(R.id.newsfeed_message_button);
        profile = (ImageView)findViewById(R.id.newsfeed_friends_button);
        notifications = (ImageView)findViewById(R.id.newsfeed_notification_button);
        postList = (ListView)findViewById(R.id.newsfeed_list);
        profilePicture = (ImageView)findViewById(R.id.newsfeed_profile_picture);
        buttons_container = (RelativeLayout)findViewById(R.id.newsfeed_buttons_container);
        profilePicture.setOnClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_notifications);
        notifications.setImageBitmap(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_profile);
        profile.setImageBitmap(bitmap);
        profile.setOnClickListener(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_messages);
        messages.setImageBitmap(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_location);
        localization.setImageBitmap(bitmap);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Notification manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Connected friend list
        dList = (ListView) findViewById(R.id.newsfeed_friends_list);
        dList.setSelector(android.R.color.holo_blue_dark);
        friends_connected_row_adapter = new Friends_connected_row_Adapter(this, friendsList);
        dList.setAdapter(friends_connected_row_adapter);

        // Persistence services
        if(sharedPreferences.contains("pictureUri")){
            GetPhoto getPhoto = new GetPhoto(sharedPreferences.getString("pictureUri", ""), null);
            try {
                bitmap = getPhoto.execute().get();
                if(bitmap != null)
                    profilePicture.setImageBitmap(RoundImage.getRoundedShape(bitmap));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }else{
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture_female);
            profilePicture.setImageBitmap(RoundImage.getRoundedShape(largeIcon));
        }
        if(sharedPreferences.contains("username")) {
            username = sharedPreferences.getString("username", "");sharedPreferences.getString("username", "");
        }

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Chat_service.MyBinder b = (Chat_service.MyBinder) binder;
                chat_service = b.getService();

                friendsList = chat_service.getFriendsList();
                if (friendsList != null) {
                    friends_connected_row_adapter = new Friends_connected_row_Adapter(Newsfeed_screen.this, friendsList);
                    dList.setAdapter(friends_connected_row_adapter);
                }else{
                    Log.e("LISTA", "nulllu");
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                chat_service = null;
            }
        };

        GetPosts getPosts = new GetPosts(username, this);
        getPosts.execute();

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

                intent.putExtra("idPet", dList.getItemAtPosition(position).toString());
                startActivity(intent);
            }

        });

        // Animation of buttons variables and events
        notificationsX = notifications.getX();
        notificationsY = notifications.getY();
        profileX = profile.getX();
        profileY = profile.getY();
        messagesX = messages.getX();
        messagesY = messages.getY();
        localizationX = localization.getX();
        localizationY = localization.getY();
        parentX = profilePicture.getX();
        parentY = profilePicture.getY();
        profilePicture.bringToFront(); // Bring profile picture to front
        buttons_container.bringToFront(); //  bring container to front

        // Start connected friends service

        connected_friends_intent = new Intent(this, Chat_service.class).setData(Uri.parse(username));
        this.bindService(connected_friends_intent, mConnection, BIND_AUTO_CREATE);

        // Refresh friend list
        friendsConnectedTimer = new Timer();
        friendsConnectedTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Newsfeed_screen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        friendsList = chat_service.getFriendsList();
                        if (friendsList != null) {
                            if (!friendsList.equals(friends_connected_row_adapter.getPetNames())){
                                Notification n = new Notification.Builder(Newsfeed_screen.this)
                                        .setContentTitle("Amigo Conectado")
                                        .setSmallIcon(R.drawable.logo_icon).build();
                                notificationManager.notify(0,n);
                            }
                            if(!friendsList.contains(username)){
                                friendsList.add(username);
                            }
                            friends_connected_row_adapter.setPetNames(friendsList);

                            friends_connected_row_adapter.notifyDataSetChanged();
                            dList.setAdapter(friends_connected_row_adapter);
                        }
                    }
                });
            }
        }, 2000, 2000);
    }


    @Override
    public void getPostsFinish(ArrayList<Post> output) {
        postList.setAdapter(new PostListAdapter(this,output));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.newsfeed_friends_button:
                intent = new Intent(this, Owner_Profile_screen.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.newsfeed_profile_picture:
                TranslateAnimation animation_notification, animation_profile, animation_messages, animation_localization;
                if (buttonsVisible == true){
                    buttonsVisible = false;
                    animation_notification = new TranslateAnimation(0, -parentX+notificationsX-90, 0, -parentY+notificationsY-30);
                    animation_profile = new TranslateAnimation(0, -parentX+profileX-90, 0, -parentY+profileY+20);
                    animation_messages = new TranslateAnimation(0, -parentX+messagesX-40, 0, -parentY+messagesY-80);
                    animation_localization = new TranslateAnimation(0, -parentX+localizationX+40, 0, -parentY+localizationY-90);
                }else {
                    animation_notification = new TranslateAnimation( 0, parentX-notificationsX+90, 0,  parentY-notificationsY+30);
                    animation_profile = new TranslateAnimation( 0, parentX-profileX+90, 0,  parentY-profileY-20);
                    animation_messages = new TranslateAnimation( 0, parentX-messagesX+40, 0,  parentY-messagesY+80);
                    animation_localization = new TranslateAnimation(0, parentX-localizationX-40, 0, parentY-localizationY+90);
                    buttonsVisible = true;
                }
                animation_notification.setDuration(500);
                animation_profile.setDuration(500);
                animation_messages.setDuration(500);
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
                animation_messages.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        messages.clearAnimation();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messages.getLayoutParams();
                        if (buttonsVisible == false) {
                            params.topMargin += (-parentY + messagesY-80);
                            params.leftMargin += (-parentX + messagesX - 40);
                        } else {
                            params.topMargin += (parentY - messagesY+80);
                            params.leftMargin += (parentX - messagesX + 40);
                        }
                        messages.setLayoutParams(params);
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
                            params.topMargin += (-parentY + profileY+20);
                            params.leftMargin += (-parentX + profileX - 90);
                        } else {
                            params.topMargin += (parentY - profileY-20);
                            params.leftMargin += (parentX - profileX + 90);
                        }
                        profile.setLayoutParams(params);
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
                            params.topMargin += (-parentY + notificationsY - 30);
                            params.leftMargin += (-parentX + notificationsX - 90);
                        } else {
                            params.topMargin += (parentY - notificationsY + 30);
                            params.leftMargin += (parentX - notificationsX + 90);
                        }
                        notifications.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                notifications.startAnimation(animation_notification);
                profile.startAnimation(animation_profile);
                messages.startAnimation(animation_messages);
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
}
