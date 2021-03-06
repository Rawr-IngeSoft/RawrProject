package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.david.rawr.Adapters.Friends_connected_row_Adapter;
import com.example.david.rawr.Adapters.SearchFriendListAdapter;
import com.example.david.rawr.IRemoteService;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Tasks.GetPhoto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
* @Requirements REQ-015
 */
public class Search_friend_screen extends Activity implements GetPhotoResponse {

    HashMap<String,Bitmap> friendsList = new HashMap<>();
    ArrayList<Pair<String, String>> searchedFriendList;
    SearchFriendListAdapter searchFriendListAdapter;
    EditText hint;
    int listPosition = 0;
    ListView friends;
    SQLiteHelper sqLiteHelper;
    SharedPreferences sharedPreferences;
    // Background service connection declaration
    private ServiceConnection mConnection;
    protected IRemoteService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteHelper = new SQLiteHelper(this);
        setContentView(R.layout.activity_search_friend);
        friends = (ListView)findViewById(R.id.search_friend_list);
        hint = (EditText) findViewById(R.id.search_friend_hint);
        final ImageView searchButton = (ImageView)findViewById(R.id.search_friend_button);
        // Se consume el metodo search friend del servicio que corre en segundo plano
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("status", "searching");
                    sqLiteHelper.clearSearchedFriends();
                    friendsList = new HashMap<String, Bitmap>();
                    service.searchFriend(hint.getText().toString());
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            searchedFriendList = sqLiteHelper.getSearchedFriends();
                            downloadPhotos();
                        }
                    }, 2000); // Este tiempo depende de la velocidad del internet
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        searchFriendListAdapter = new SearchFriendListAdapter(this, friendsList, searchedFriendList, sharedPreferences.getString("petUsername",""));
        friends.setAdapter(searchFriendListAdapter);

        // Conexion con el servicio que corre en segundo plano
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
        // Start connected friends service
        if (service == null) {
            Intent connected_friends_intent = new Intent();
            ActivityManager am = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
            // Return a list of the tasks that are currently running,
            // with the most recent being first and older ones after in order.
            // Taken 1 inside getRunningTasks method means want to take only
            // top activity from stack and forgot the olders.
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

            ComponentName componentInfo = taskInfo.get(0).topActivity;
            componentInfo.getPackageName();
            connected_friends_intent.setPackage(componentInfo.getPackageName());
            connected_friends_intent.setAction("service.Chat");
            this.bindService(connected_friends_intent, mConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_friend, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);

    }

    /**
     * Descarga las fotos de las mascotas encontradas al buscar por una palabra
     * y las despliega en la vista
     */
    private void downloadPhotos(){
        listPosition = 0;
        for ( int i = 0; i < searchedFriendList.size(); i++){
            if(searchedFriendList.get(i).second.compareTo("null") != 0) {
                GetPhoto getPhoto = new GetPhoto(searchedFriendList.get(i).second, searchedFriendList.get(i).first, this);
                getPhoto.execute();
                while (getPhoto.getStatus() != AsyncTask.Status.FINISHED) ;
            }else{
                friendsList.put(searchedFriendList.get(listPosition).first, BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture_female));
                listPosition++;
            }
        }
        searchFriendListAdapter = new SearchFriendListAdapter(Search_friend_screen.this, friendsList, searchedFriendList, sharedPreferences.getString("petUsername",""));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                friends.setAdapter(searchFriendListAdapter);
            }
        });

    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        friendsList.put(searchedFriendList.get(listPosition).first, bitmap);
        listPosition++;
    }
}
