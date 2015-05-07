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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.david.rawr.Adapters.Friends_connected_row_Adapter;
import com.example.david.rawr.Adapters.PostListAdapter;
import com.example.david.rawr.Interfaces.GetPostsResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.db.GetPhoto;
import com.example.david.rawr.db.GetPosts;
import com.example.david.rawr.models.Pet;
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

    ImageView localization, messages, friends, notifications, profilePicture;
    ListView postList;
    SharedPreferences sharedPreferences;
    com.github.nkzawa.socketio.client.Socket mySocket;
    Emitter.Listener startSession_listener;
    String username = "";
    Friends_connected_row_Adapter friends_connected_row_adapter;
    DrawerLayout dLayout;
    ListView dList;
    ArrayList<String> friendsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newsfeed_screen);
        localization = (ImageView)findViewById(R.id.newsfeed_localization_button);
        messages = (ImageView)findViewById(R.id.newsfeed_message_button);
        friends = (ImageView)findViewById(R.id.newsfeed_friends_button);
        notifications = (ImageView)findViewById(R.id.newsfeed_notification_button);
        postList = (ListView)findViewById(R.id.newsfeed_list);
        profilePicture = (ImageView)findViewById(R.id.newsfeed_profile_picture);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_notifications);
        notifications.setImageBitmap(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_profile);
        friends.setImageBitmap(bitmap);
        friends.setOnClickListener(this);
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
            profilePicture.setImageResource(R.drawable.default_profile_picture_female);
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

        // Connected friends code
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
        dList.addHeaderView(friendsHeader);
        dList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

                dLayout.closeDrawers();
                Intent intent = new Intent(Newsfeed_screen.this, Chat_window.class);
                intent.putExtra("idPet", dList.getItemAtPosition(position).toString());
                startActivity(intent);
            }

        });
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
