package com.taylorrayhoward.taylor.spotifyalarm;

/**
 * Created by Thoward on 11/24/2016.
 */

public class Alarm {


    private String hour;
    private String minute;
    private int id;
    private String time;

    String playlistName;
    String playlistId;
    String playlistURI;
    boolean twentyFourHourTime = false;

    public Alarm(String hour, String minute, int id, String playlistName, String playlistId, String playlistURI) {
        this.hour = hour;
        this.minute = minute;
        this.id = id;
        this.playlistName = playlistName;
        this.playlistId = playlistId;
        this.playlistURI = playlistURI;
        time = hour + ":" + minute;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public String getPlaylistName() {
        return playlistName;
    }
    public String getPlaylistId() {
        return playlistId;
    }
    public String getPlaylistURI() {
        return playlistURI;
    }

}
