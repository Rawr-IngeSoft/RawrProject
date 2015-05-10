package com.example.david.rawr.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.example.david.rawr.R;
import com.example.david.rawr.models.Message;
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

public  class Chat_service extends Service {

    Socket mySocket;
    Emitter.Listener startSession_listener, chat_message_listener;
    String username;
    ArrayList<String> friendsList;
    IBinder iBinder = new MyBinder();
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mySocket = IO.socket("http://178.62.233.249:3000");
            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            JSONObject data = new JSONObject();
            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            startSession_listener = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject response = new JSONObject((String) args[0]);
                        JSONArray jsonArray = (JSONArray) response.get("users");
                        friendsList = new ArrayList<String>();
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

            chat_message_listener = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject data = (JSONObject)args[0];
                        Message msg = new Message((String)data.get("sender"),(String)data.get("message"));
                        Notification n = new Notification.Builder(Chat_service.this)
                                    .setSmallIcon(R.drawable.logo_icon)
                                    .setContentTitle(msg.getPerson())
                                    .setContentText(msg.getMessage()).build();
                        notificationManager.notify(0,n);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            username=sharedPreferences.getString("username", "");
            data.put("username", username);
            friendsList= new ArrayList();
            mySocket.on("chat_message", chat_message_listener);
            mySocket.on("response_start_session", startSession_listener);
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
        public Chat_service getService(){
            return Chat_service.this;
        }
    }

    public ArrayList<String> getFriendsList(){
        return friendsList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mySocket.disconnect();
        mySocket.off("response_start_session",startSession_listener);
        mySocket.off("chat_message", chat_message_listener);
        mySocket = null;
    }
}
