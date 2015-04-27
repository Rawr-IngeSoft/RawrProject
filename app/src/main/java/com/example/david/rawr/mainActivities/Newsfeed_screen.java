package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.david.rawr.Adapters.PostListAdapter;
import com.example.david.rawr.Interfaces.GetPostsResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.db.GetPhoto;
import com.example.david.rawr.db.GetPosts;
import com.example.david.rawr.models.Post;
import com.example.david.rawr.otherClasses.RoundImage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Newsfeed_screen extends Activity implements GetPostsResponse {

    ImageView localization, messages, friends, notifications, profilePicture;
    ListView postList;
    SharedPreferences sharedPreferences;
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
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_messages);
        messages.setImageBitmap(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_location);
        localization.setImageBitmap(bitmap);
        GetPosts getPosts = new GetPosts("dug", this);
        getPosts.execute();
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
        }
    }


    @Override
    public void getPostsFinish(ArrayList<Post> output) {
        postList.setAdapter(new PostListAdapter(this,output));
    }
}
