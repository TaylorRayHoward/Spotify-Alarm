package com.taylorrayhoward.taylor.spotifyalarm;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.R.attr.id;
import static android.R.attr.paddingEnd;
import static com.taylorrayhoward.taylor.spotifyalarm.MainActivity.am;

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

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View outerView = inflater.inflate(R.layout.alarm_row, parent, false);
        //TODO let the user pick 24 hour time or 12 hour time in the options, for now default to 12
        String time = getItem(position).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        TextView timeText = (TextView) outerView.findViewById(R.id.timeText);
        try {
            Date date = sdf.parse(time);
            timeText.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String playlistName = getItem(position).getPlaylistName();

        final long alarmId = getItem(position).getId();

        final int hour = Integer.parseInt(getItem(position).getHour());
        final int minute = Integer.parseInt(getItem(position).getMinute());

        Switch alarmOnSwitch = (Switch) outerView.findViewById(R.id.alarm_on_switch);
        alarmOnSwitch.setChecked(getItem(position).getEnabled() == 1);
        alarmOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent alarmIntent = new Intent(compoundButton.getContext(), AlarmReceiver.class);
                if(!b){
                    PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(compoundButton.getContext(),
                            (int)alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.cancel(alarmPendingIntent);
                    db.setEnable((int)alarmId, b);
                }
                else{
                    Calendar cal = new GregorianCalendar();
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, minute);
                    PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(compoundButton.getContext(),
                            (int)alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmPendingIntent);
                }
            }
        });
        //TextView playlistText = (TextView) outerView.findViewById(R.id.playlistText);
        //playlistText.setText(playlistName);
        return outerView;
    }

    @Override
    public Alarm getItem(int position){
        return data.get(position);
    }


}
