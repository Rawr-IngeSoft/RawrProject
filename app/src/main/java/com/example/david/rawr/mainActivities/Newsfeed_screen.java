package com.example.david.rawr.MainActivities;

import android.app.Activity;
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
import com.example.david.rawr.db.GetPosts;
import com.example.david.rawr.models.Post;
import com.example.david.rawr.otherClasses.RoundImage;

import java.util.ArrayList;


public class Newsfeed_screen extends Activity implements GetPostsResponse {

    ImageView localization, messages, friends, notifications;
    ListView postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_screen);
        localization = (ImageView)findViewById(R.id.newsfeed_localization_button);
        messages = (ImageView)findViewById(R.id.newsfeed_message_button);
        friends = (ImageView)findViewById(R.id.newsfeed_friends_button);
        notifications = (ImageView)findViewById(R.id.newsfeed_notification_button);
        postList = (ListView)findViewById(R.id.newsfeed_list);
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notifications_button);
        notifications.setImageBitmap(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.friends_button);
        friends.setImageBitmap(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.messages_button);
        messages.setImageBitmap(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.localization_button);
        localization.setImageBitmap(bitmap);*/
        GetPosts getPosts = new GetPosts("dug", this);
        getPosts.execute();
    }


    @Override
    public void getPostsFinish(ArrayList<Post> output) {
        postList.setAdapter(new PostListAdapter(this,output));
    }
}
