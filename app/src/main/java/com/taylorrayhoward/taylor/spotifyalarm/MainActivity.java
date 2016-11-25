package com.taylorrayhoward.taylor.spotifyalarm;

import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import kaaes.spotify.webapi.android.SpotifyApi;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;
import static com.taylorrayhoward.taylor.spotifyalarm.Info.CLIENT_ID;
import static com.taylorrayhoward.taylor.spotifyalarm.Info.REDIRECT_URI;

public class MainActivity extends AppCompatActivity implements ConnectionStateCallback {
    //TODO Add database, add custom rows, add on click rows, finish whole project
    public String accessToken;
    public SpotifyApi api = new SpotifyApi();
    private AlarmDBHelper db = new AlarmDBHelper(this);
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateAlarmList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("AlarmTimes", "test");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                Log.d("AlarmTimes", "test");
                                db.insertAlarm(String.valueOf(hour), String.valueOf(minute), "");
                                generateAlarmList();
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }

        });
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "playlist-read-private"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }



    private void generateAlarmList() {
//        String[] s1 = Arrays.copyOf(db.getAlarmTimes().toArray(), db.getAlarmTimes().size(), String[].class);
        ArrayList<Alarm> alarmList = db.getAllData();
        for (Alarm a : alarmList) {
            Log.d("AlarmTimes", a.getTime());
        }
        ListView listView = (ListView) findViewById(R.id.alarm_listview);
        listAdapter = new AlarmAdapter(this, alarmList);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                accessToken = response.getAccessToken();
                api.setAccessToken(response.getAccessToken());
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("Login", "Succesfully logged in");
        Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(int i) {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }
}
