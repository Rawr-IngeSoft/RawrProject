package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.david.rawr.R;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class Chat_window extends Activity implements View.OnClickListener{

    String receiver, username;
    SharedPreferences sharedPreferences;
    EditText message;
    Button send_button;
    com.github.nkzawa.socketio.client.Socket mySocket;
    JSONObject data;
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
        try {
            mySocket = IO.socket("http://178.62.233.249:3000");
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
                    data.put("message", message.getText());
                    message.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mySocket.emit("start_session", data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mySocket.disconnect();
    }

}
