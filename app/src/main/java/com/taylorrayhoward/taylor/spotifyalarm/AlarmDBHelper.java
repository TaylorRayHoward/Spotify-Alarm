package com.taylorrayhoward.taylor.spotifyalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by Thoward on 11/24/2016.
 */

public class AlarmDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AlarmDBHelper.db";
    public static final String ALARM_TABLE_NAME = "alarms";
    public static final String ALARM_COLUMN_ID = "id";
    public static final String ALARM_COLUMN_HOUR = "hour";
    public static final String ALARM_COLUMN_MINUTE = "minute";
    public static final String ALARM_COLUMN_DAYS = "days";
    public static final String ALARM_COLUMN_PLAYLIST_NAME = "playlist_name";
    public static final String ALARM_COLUMN_PLAYLIST_ID = "playlist_id";
    public static final String ALARM_COLUMN_PLAYLIST_OWNER = "playlist_owner";
    public static final int ALARM_VERSION = 1;


    public AlarmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, ALARM_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(
//                "create table alarms " +
//                        "(id integer primary key, hour text, minute text, days text)"
//        );

        db.execSQL(
                //"create table " +ALARM_TABLE_NAME +" " +"(id integer primary key, hour text, minute text, days text)"
                String.format("create table %1$s (%2$s integer primary key, %3$s text, %4$s text, " +
                                "%5$s text, %6$s text, %7$s text, %8$s text)", ALARM_TABLE_NAME,
                        ALARM_COLUMN_ID, ALARM_COLUMN_HOUR, ALARM_COLUMN_MINUTE, ALARM_COLUMN_DAYS,
                        ALARM_COLUMN_PLAYLIST_NAME, ALARM_COLUMN_PLAYLIST_ID, ALARM_COLUMN_PLAYLIST_OWNER)
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAlarm(String hour, String minute, String days, String name, String id, String owner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALARM_COLUMN_DAYS, days);
        contentValues.put(ALARM_COLUMN_HOUR, hour);
        contentValues.put(ALARM_COLUMN_MINUTE, minute);
        contentValues.put(ALARM_COLUMN_PLAYLIST_NAME, name);
        contentValues.put(ALARM_COLUMN_PLAYLIST_ID, id);
        contentValues.put(ALARM_COLUMN_PLAYLIST_OWNER, owner);
        db.insert(ALARM_TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList<Alarm> getAllData(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery("select * from " + ALARM_TABLE_NAME, null);
        c.moveToFirst();
        ArrayList<Alarm> r = new ArrayList<>();
        while (!c.isAfterLast()) {
            String hour = c.getString(c.getColumnIndex(ALARM_COLUMN_HOUR));
            String minute = c.getString(c.getColumnIndex(ALARM_COLUMN_MINUTE));
            int id = c.getInt(c.getColumnIndex(ALARM_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(ALARM_COLUMN_PLAYLIST_NAME));
            String owner = c.getString(c.getColumnIndex(ALARM_COLUMN_PLAYLIST_OWNER));
            String playlist_id = c.getString(c.getColumnIndex(ALARM_COLUMN_PLAYLIST_ID));

            r.add(new Alarm(hour, minute, id, name, playlist_id, owner));
            c.moveToNext();
        }
        c.close();
        return r;
    }

    public boolean deleteAlarm(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(ALARM_TABLE_NAME, ALARM_COLUMN_ID + "=" + id, null) > 0;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + ALARM_TABLE_NAME + " where id=" + id + "", null);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, ALARM_TABLE_NAME);
    }

    public ArrayList<String> getAlarmTimes() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ALARM_TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(ALARM_COLUMN_HOUR)) + ":"
                    + res.getString(res.getColumnIndex(ALARM_COLUMN_MINUTE)));
            res.moveToNext();
        }
        return array_list;
    }

}
