package com.example.david.rawr.Communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.david.rawr.Services.Chat_service;

/**
 * Created by david on 10/05/2015.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, Chat_service.class);
        context.startService(myIntent);
    }
}
