package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.david.rawr.Adapters.MessagesListAdapter;
import com.example.david.rawr.R;
import com.github.nkzawa.emitter.Emitter;
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
    com.github.nkzawa.socketio.client.Socket mySocket;
    JSONObject data;
    Emitter.Listener chat_message_listener;
    ListView messagesList;
    ArrayList<String> messages = new ArrayList<>();
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
        try {
            mySocket = IO.socket("http://178.62.233.249:3000");
            chat_message_listener = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        data = (JSONObject)args[0];
                        final String msg = (String)data.get("message");
                        Chat_window.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messagesListAdapter.getData().add(msg);
                                messagesListAdapter.notifyDataSetChanged();
                                messagesList.setAdapter(messagesListAdapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            mySocket.on("chat_message", chat_message_listener);
            mySocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
                    message.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mySocket.emit("chat_message", data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        mySocket.disconnect();
        mySocket.off("chat_message", chat_message_listener);
    }
}