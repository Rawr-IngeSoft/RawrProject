package com.example.david.rawr.Communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.david.rawr.Services.Chat_service;

/**
 * Created by david on 10/05/2015.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("action", action);
        Intent myIntent = new Intent(context, Chat_service.class);
        context.startService(myIntent);
    }
}
