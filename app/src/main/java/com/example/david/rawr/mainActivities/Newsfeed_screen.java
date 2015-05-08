package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import com.example.david.rawr.db.GetPhoto;
import com.example.david.rawr.db.GetPosts;
import com.example.david.rawr.models.Post;
import com.example.david.rawr.otherClasses.RoundImage;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Newsfeed_screen extends Activity implements GetPostsResponse, View.OnClickListener {

    ImageView localization, messages, profile, notifications, profilePicture;
    ListView postList;
    SharedPreferences sharedPreferences;
    com.github.nkzawa.socketio.client.Socket mySocket;
    Emitter.Listener startSession_listener;
    String username = "";
    Friends_connected_row_Adapter friends_connected_row_adapter;
    DrawerLayout dLayout;
    ListView dList;
    boolean buttonsVisible = true;
    ArrayList<String> friendsList = new ArrayList<>();
    float  notificationsX, notificationsY,parentX, parentY, profileX, profileY, messagesX, messagesY, localizationX, localizationY;
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
        profilePicture.setOnClickListener(this);
        profilePicture.bringToFront();
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
            username = sharedPreferences.getString("username", "");
        }
        try {
            mySocket = IO.socket("http://178.62.233.249:3000");
            startSession_listener = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject response = new JSONObject((String)args[0]);
                        JSONArray jsonArray = (JSONArray)response.get("users");
                        friendsList = new ArrayList<>();
                        if (jsonArray != null) {
                            for (int i=0;i<jsonArray.length();i++) {
                                friendsList.add(jsonArray.get(i).toString());
                            }
                        }
                        friends_connected_row_adapter.setPetNames(friendsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            mySocket.on("response_start_session", startSession_listener);
            mySocket.connect();
            JSONObject data = new JSONObject();
            data.put("username", username);
            mySocket.emit("start_session", data);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetPosts getPosts = new GetPosts(username, this);
        getPosts.execute();

        dLayout = (DrawerLayout) findViewById(R.id.newsfeed_friends_sliding_list);
        dList = (ListView) findViewById(R.id.newsfeed_friends_list);
        dList.setSelector(android.R.color.holo_blue_dark);
        friends_connected_row_adapter = new Friends_connected_row_Adapter(Newsfeed_screen.this, friendsList);
        dList.setAdapter(friends_connected_row_adapter);
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

        // Animation of buttons variables
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
        mySocket.disconnect();
        mySocket.off("response_start_session", startSession_listener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
