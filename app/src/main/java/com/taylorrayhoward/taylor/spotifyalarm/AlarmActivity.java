package com.taylorrayhoward.taylor.spotifyalarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import static com.taylorrayhoward.taylor.spotifyalarm.Info.CLIENT_ID;
import static com.taylorrayhoward.taylor.spotifyalarm.Info.REDIRECT_URI;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class AlarmActivity extends AppCompatActivity implements ConnectionStateCallback, SpotifyPlayer.NotificationCallback  {
    String ownerid;
    String playlistId;
    private Player mPlayer;
    private String songUri;
    public SpotifyApi api = new SpotifyApi();
    SpotifyService spotify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        ownerid = getIntent().getStringExtra("playlistOwner");
        playlistId = getIntent().getStringExtra("playlistId");
        builder.setScopes(new String[]{"user-read-private", "streaming", "playlist-read-private"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        spotify = api.getService();
        Button b = (Button) findViewById(R.id.stop_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                api.setAccessToken(response.getAccessToken());
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(AlarmActivity.this);
                        mPlayer.addNotificationCallback(AlarmActivity.this);
                        try {
                            new getSongSync().execute().get(10, TimeUnit.SECONDS);
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            e.printStackTrace(); //TODO Handle if the phone wont get the song by playing a regular sound from the phone
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn() {
        mPlayer.playUri(null, songUri,0, 0);
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Spotify.destroyPlayer(this);
    }

    private class getSongSync extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            Pager<PlaylistTrack> songs = spotify.getPlaylistTracks(ownerid, playlistId);
            Random rand = new Random(Calendar.getInstance().getTimeInMillis());
            int x = rand.nextInt(songs.items.size());
            songUri = songs.items.get(x).track.uri;
            return 1;

        }
    }
}
