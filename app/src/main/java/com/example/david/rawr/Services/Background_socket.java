package com.example.david.rawr.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;

import com.example.david.rawr.IRemoteService;
import com.example.david.rawr.MainActivities.Chat_window;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by david on 09/05/2015.
 */

public  class Background_socket extends Service {

    Socket mySocket = null;
    Emitter.Listener startSession_listener, chat_message_listener, notification_listener;
    String petUsername;
    ArrayList<String> friendsList;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    HashMap<String,String> notifications;
    NotificationCompat.InboxStyle inboxStyle;
    SharedPreferences sharedPreferences;
    SQLiteHelper sqLiteHelper;

    IRemoteService.Stub myBinder = new IRemoteService.Stub() {
        @Override
        public List<String> getFriendsList() throws RemoteException {
            return friendsList;
        }

        @Override
        public void sendMessage(String sender, String receiver, String msg) throws RemoteException {
            JSONObject data = new JSONObject();
            Calendar c = Calendar.getInstance();
            String date = c.get(Calendar.DAY_OF_MONTH) + "- " + c.get(Calendar.MONTH) + c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE);
            try {
                data.put("sender", sender);
                data.put("receiver", receiver);
                data.put("message", msg);
                data.put("date",date);
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

        @Override
        public void sendFriendRequest(String sender, String receiver) throws RemoteException {
            JSONObject data = new JSONObject();
            try {
                data.put("sender", sender);
                data.put("receiver",receiver);
                mySocket.emit("notification", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };

    // Binder for communication
    @Override
    public void onCreate() {
        super.onCreate();
        sqLiteHelper = new SQLiteHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            notifications = new HashMap<>();
            notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            inboxStyle = new NotificationCompat.InboxStyle();
            notificationBuilder = new NotificationCompat.Builder(Background_socket.this)
                    .setSmallIcon(R.drawable.logo_icon)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(inboxStyle)
                    .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification_sound));
            mySocket = IO.socket("http://178.62.233.249:3000");
            final JSONObject data = new JSONObject();
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
                        // Deploy notification
                        inboxStyle.addLine(data.getString("message"));
                        Intent intentNotification = new Intent(Background_socket.this, Chat_window.class);
                        intentNotification.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(Background_socket.this,0, intentNotification,0);
                        notificationBuilder.setContentTitle(data.getString("sender"));
                        notificationBuilder.setContentIntent(pendingIntent);
                        notificationBuilder.setStyle(inboxStyle);
                        notificationManager.notify(0, notificationBuilder.build());
                        sharedPreferences.edit().putString("receiver",data.getString("sender") ).commit();
                        // TODO write in sqlite
                        String senderPofilePic = sqLiteHelper.getProfilePicturePath((String)data.get("sender"));
                        sqLiteHelper.addMessage(new Message((String)data.getString("message"), (String)data.getString("sender"), (String)data.getString("receiver"), "unread",(String)data.getString("date"),senderPofilePic ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            notification_listener = new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            };
            petUsername=sharedPreferences.getString("petUsername", "");
            data.put("username", petUsername);
            friendsList= new ArrayList();
            mySocket.on("chat_message", chat_message_listener);
            mySocket.on("response_start_session", startSession_listener);
            mySocket.on("notification",notification_listener);
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
        return super.onUnbind(intent);
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
