package com.example.david.rawr.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by david on 09/05/2015.
 */

public class Connected_friends_listener extends Service {

    Socket mySocket;
    Emitter.Listener startSession_listener;
    String username;
    ArrayList<String> friendsList;
    IBinder iBinder = new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            username = intent.getDataString();
            startSession_listener = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject response = new JSONObject((String) args[0]);
                        JSONArray jsonArray = (JSONArray) response.get("users");
                        friendsList = new ArrayList<>();
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                friendsList.add(jsonArray.get(i).toString());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            mySocket = IO.socket("http://178.62.233.249:3000");
            mySocket.on("response_start_session", startSession_listener);
            JSONObject data = new JSONObject();
            data.put("username", username);
            mySocket.connect();
            mySocket.emit("start_session", data);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MyBinder extends Binder {
        public Connected_friends_listener getService(){
            return Connected_friends_listener.this;
        }
    }

    public ArrayList<String> getFriendsList(){
        return friendsList;
    }
}
