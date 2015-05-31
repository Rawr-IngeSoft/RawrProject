package com.example.david.rawr.Communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.david.rawr.Services.Background_socket;

/**
 * Created by david on 10/05/2015.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent myIntent = new Intent(context, Background_socket.class);
        context.startService(myIntent);
    }
}
