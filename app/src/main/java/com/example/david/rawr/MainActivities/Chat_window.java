package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.david.rawr.Adapters.Friends_connected_row_Adapter;
import com.example.david.rawr.Adapters.MessagesListAdapter;
import com.example.david.rawr.R;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.Services.Chat_service;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class Chat_window extends Activity implements View.OnClickListener{

    String receiver, username;
    SharedPreferences sharedPreferences;
    EditText message;
    Button send_button;
    JSONObject data;
    ListView messagesList;
    // Background service connection declaration
    ServiceConnection mConnection;
    private Chat_service chat_service;

    ArrayList<Message> messages = new ArrayList<>();
    MessagesListAdapter messagesListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("username")) {
            username = sharedPreferences.getString("username", "");
        }
        receiver = getIntent().getStringExtra("idPet");
        send_button = (Button) findViewById(R.id.chat_window_send_button);
        send_button.setOnClickListener(this);
        message = (EditText) findViewById(R.id.chat_window_message);
        messagesList = (ListView) findViewById(R.id.chat_window_messages_list);
        messagesListAdapter = new MessagesListAdapter(this, messages);
        messagesList.setAdapter(messagesListAdapter);
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Chat_service.MyBinder b = (Chat_service.MyBinder) binder;
                chat_service = b.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                chat_service = null;
            }
        };
        Intent connected_friends_intent = new Intent(this, Chat_service.class).setData(Uri.parse(username));
        this.bindService(connected_friends_intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chat_window_send_button:
                data = new JSONObject();
                try {
                    data.put("sender", username);
                    data.put("receiver", receiver);
                    data.put("message", message.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Chat_window.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messagesListAdapter.getData().add(new Message(message.getText().toString(), username));
                        messagesListAdapter.notifyDataSetChanged();
                        messagesList.setAdapter(messagesListAdapter);
                    }
                });
                message.setText("");
                chat_service.sendMessage(data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
