package com.cad.halstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, FileWatcherService.class);
        context.startService(serviceIntent);
        Log.i("BootCompletedReceiver", "FileWatcherService Started at boot time");
    }
}