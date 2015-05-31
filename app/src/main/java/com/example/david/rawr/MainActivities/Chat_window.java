package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.Adapters.MessagesListAdapter;
import com.example.david.rawr.Adapters.PostListAdapter;
import com.example.david.rawr.IRemoteService;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Tasks.GetPhoto;
import com.example.david.rawr.otherClasses.RoundImage;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Chat_window extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, GetPhotoResponse{

    String receiver, petUsername;
    SharedPreferences sharedPreferences;
    EditText message;
    ImageView send_button;
    String pictureUri;
    ListView messagesList;
    SQLiteHelper SQLiteHelper;
    ImageView petPicture;
    Bitmap senderBitmap = null,receiverBitmap = null;
    com.example.david.rawr.SQLite.SQLiteHelper sqLiteHelper;
    // Background service connection declaration
    ServiceConnection mConnection;
    protected IRemoteService service;

    static ArrayList<Message> messages;
    MessagesListAdapter messagesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO consume service to mark read all messages of the friend
        super.onCreate(savedInstanceState);
        SQLiteHelper = new SQLiteHelper(this);
        setContentView(R.layout.activity_chat_window);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("petUsername")) {
            petUsername = sharedPreferences.getString("petUsername", "");
        }
        if(sharedPreferences.contains("receiver")) {
            receiver = sharedPreferences.getString("receiver", "");
        }
        if (petUsername.compareTo(receiver) == 0){
            messages = SQLiteHelper.getMyMessages(petUsername);
        }else {
            messages = SQLiteHelper.getMessagesOf(receiver);
        }
        checkSenderVisibility();
        TextView petLabel = (TextView)findViewById(R.id.chat_window_petLabel);
        petPicture = (ImageView)findViewById(R.id.chat_window_profilePic);
        petLabel.setText(petUsername);;
        send_button = (ImageView) findViewById(R.id.chat_window_send_button);
        send_button.setOnClickListener(this);
        message = (EditText) findViewById(R.id.chat_window_message);
        messagesList = (ListView) findViewById(R.id.chat_window_messages_list);

        // Get friend photo
        sqLiteHelper = new SQLiteHelper(this);
        GetPhoto getPhoto = new GetPhoto(sqLiteHelper.getFriendPhotoPath(receiver), receiver, this);
        getPhoto.execute();
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
        messagesList.setOnItemClickListener(this);
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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Chat_window.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (petUsername.compareTo(receiver) == 0) {
                            messages = SQLiteHelper.getMyMessages(petUsername);
                        } else {
                            messages = SQLiteHelper.getMessagesOf(receiver);
                        }
                        messagesListAdapter = new MessagesListAdapter(Chat_window.this, messages, petUsername, senderBitmap,receiverBitmap);
                        messagesList.setAdapter(messagesListAdapter);
                    }
                });
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chat_window_send_button:
                try {
                    if(message.getText().toString().trim().length() > 0) {
                        service.sendMessage(petUsername, receiver, message.getText().toString());
                        Chat_window.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar calendar = Calendar.getInstance();
                                String dateString = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
                                Message message1 = new Message(message.getText().toString(), petUsername, receiver, "read", dateString, pictureUri);
                                messages.add(message1);
                                messagesListAdapter.setData(messages);
                                messagesList.setAdapter(messagesListAdapter);
                                SQLiteHelper.addMessage(message1);
                            }
                        });
                        message.setText("");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private static void checkSenderVisibility(){

        for (int i = 0; i < messages.size(); i++){
            if(i > 0){
                Log.e(messages.get(i).getSender(),messages.get(i-1).getSender() );
                if(messages.get(i).getSender().compareTo(messages.get(i-1).getSender()) == 0){
                    messages.get(i).setVisible(false);
                }else{
                    messages.get(i).setVisible(true);
                }
            }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView dateTV = (TextView)view.findViewById(R.id.messages_list_row_date);
        LinearLayout.LayoutParams layoutParams;
        if(dateTV.getVisibility() == View.VISIBLE){
            dateTV.setVisibility(View.INVISIBLE);
            layoutParams  = new LinearLayout.LayoutParams(dateTV.getWidth(),1);
            dateTV.setLayoutParams(layoutParams);
        }else{
            layoutParams  = new LinearLayout.LayoutParams(dateTV.getWidth(),30);
            dateTV.setVisibility(View.VISIBLE);
            dateTV.setLayoutParams(layoutParams);
            dateTV.setText(messages.get(position).getDate());
        }

    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        if(senderBitmap == null){
            senderBitmap = bitmap;
            Log.e(petUsername, sharedPreferences.getString("petPicture",""));
            GetPhoto getPhoto = new GetPhoto(sharedPreferences.getString("petPicture",""), petUsername, this);
            getPhoto.execute();
        }else {
            receiverBitmap = bitmap;
            petPicture.setImageBitmap(RoundImage.getRoundedShape(receiverBitmap));
            messagesListAdapter = new MessagesListAdapter(this, messages, petUsername, senderBitmap, receiverBitmap);
            messagesList.setAdapter(messagesListAdapter);
        }
    }
}
