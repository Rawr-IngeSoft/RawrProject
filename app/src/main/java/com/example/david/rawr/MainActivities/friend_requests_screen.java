package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.david.rawr.Adapters.FriendsRequestAdapter;
import com.example.david.rawr.Interfaces.GetFriendRequestsResponse;
import com.example.david.rawr.Models.FriendRequest;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;

import java.util.ArrayList;


public class Friend_requests_screen extends Activity implements GetFriendRequestsResponse {

    FriendsRequestAdapter friendsRequestAdapter;
    SQLiteHelper SQLiteHelper;
    ArrayList<FriendRequest> friendRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests_screen);
        SQLiteHelper = new SQLiteHelper(this);
        ListView requests = (ListView) findViewById(R.id.friend_requests_list);
        friendRequests = SQLiteHelper.getFriendRequests();
        friendsRequestAdapter = new FriendsRequestAdapter(friendRequests, this);
        requests.setAdapter(friendsRequestAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_requests_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getRequestsFinish(ArrayList<FriendRequest> output) {

    }
}
