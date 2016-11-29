package com.taylorrayhoward.taylor.spotifyalarm;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by Thoward on 11/21/2016.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {
    private AlarmDBHelper db = new AlarmDBHelper(getContext());
    private ArrayList<Alarm> data;

    public AlarmAdapter(Context context, ArrayList<Alarm> resource) {
        super(context, R.layout.alarm_row, resource);
        data = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View outerView = inflater.inflate(R.layout.alarm_row, parent, false);
        String time = getItem(position).getTime();
        String playlistName = getItem(position).getPlaylistName();
        TextView timeText = (TextView) outerView.findViewById(R.id.timeText);
        timeText.setText(time);
        TextView playlistText = (TextView) outerView.findViewById(R.id.playlistText);
        playlistText.setText(playlistName);
        return outerView;
    }

    @Override
    public Alarm getItem(int position){
        return data.get(position);
    }


}
