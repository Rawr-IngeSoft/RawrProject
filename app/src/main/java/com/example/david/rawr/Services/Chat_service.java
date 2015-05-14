package com.example.david.rawr.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.david.rawr.IRemoteService;
import com.example.david.rawr.MainActivities.Chat_window;
import com.example.david.rawr.R;
import com.example.david.rawr.Models.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 09/05/2015.
 */

public  class Chat_service extends Service {

    Socket mySocket = null;
    Emitter.Listener startSession_listener, chat_message_listener;
    String petUsername;
    ArrayList<String> friendsList;
    NotificationManager notificationManager;
    IRemoteService.Stub myBinder = new IRemoteService.Stub() {
        @Override
        public List<String> getFriendsList() throws RemoteException {
            return friendsList;
        }

        @Override
        public void sendMessage(String sender, String receiver, String msg) throws RemoteException {
            JSONObject data = new JSONObject();
            Log.e("sender", sender);
            Log.e("receiver", receiver);
            try {
                data.put("sender", sender);
                data.put("receiver", receiver);
                data.put("message", msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mySocket.emit("chat_message", data);
        }

        @Override
        public void changePet(String petUsername) throws RemoteException {
            JSONObject data = new JSONObject();
            try {
                data.put("username", petUsername);
                mySocket.emit("start_session", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };

    // Binder for communication
    @Override
    public void onCreate() {
        Log.e("status","on_create");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.e("status","starting_service");
            mySocket = IO.socket("http://178.62.233.249:3000");
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
                        Log.e("status", "listen_msg");
                        JSONObject data = (JSONObject)args[0];
                        Intent intent = new Intent(getApplicationContext(), Chat_window.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, intent,0);
                        Message msg = new Message((String)data.get("message"),(String)data.get("sender"));
                        Notification notification = new Notification.Builder(Chat_service.this)
                                    .setSmallIcon(R.drawable.logo_icon)
                                    .setContentTitle(msg.getPerson())
                                    .setContentText(msg.getMessage())
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntent).build();
                        notificationManager.notify(0,notification);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            petUsername=sharedPreferences.getString("petUsername", "");
            data.put("username", petUsername);
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
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("status", "unbinded");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("status", "destroyed");
        super.onDestroy();
        mySocket.disconnect();
        mySocket.off("response_start_session",startSession_listener);
        mySocket.off("chat_message", chat_message_listener);
        mySocket = null;
    }
}
