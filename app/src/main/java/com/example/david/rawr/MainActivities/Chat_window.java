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
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.david.rawr.Adapters.Friends_connected_row_Adapter;
import com.example.david.rawr.Adapters.MessagesListAdapter;
import com.example.david.rawr.IRemoteService;
import com.example.david.rawr.R;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.Services.Chat_service;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class Chat_window extends Activity implements View.OnClickListener{

    String receiver, petUsername;
    SharedPreferences sharedPreferences;
    EditText message;
    Button send_button;
    JSONObject data;
    ListView messagesList;
    // Background service connection declaration
    ServiceConnection mConnection;
    protected IRemoteService service;

    ArrayList<Message> messages = new ArrayList<>();
    MessagesListAdapter messagesListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("petUsername")) {
            petUsername = sharedPreferences.getString("petUsername", "");
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
            connected_friends_intent.setAction("service.Chat");
            this.bindService(connected_friends_intent, mConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chat_window_send_button:
                try {
                    Log.e("status", "click_btn");
                    service.sendMessage(petUsername,receiver,message.getText().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Chat_window.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messagesListAdapter.getData().add(new Message(message.getText().toString(), petUsername));
                        messagesListAdapter.notifyDataSetChanged();
                        messagesList.setAdapter(messagesListAdapter);
                    }
                });
                message.setText("");
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
