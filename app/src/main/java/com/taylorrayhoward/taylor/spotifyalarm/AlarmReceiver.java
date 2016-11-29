package com.taylorrayhoward.taylor.spotifyalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Thoward on 11/28/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String playlistId = intent.getStringExtra("playlistId");
        String playlistOwner = intent.getStringExtra("playlistOwner");
        Intent i = new Intent(context.getApplicationContext(), AlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("playlistId", playlistId);
        i.putExtra("playlistOwner", playlistOwner);
        context.startActivity(i);
    }
}
