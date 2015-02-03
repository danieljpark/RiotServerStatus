package com.example.danielpark2.riotapp.API;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;
import android.util.Log;

import com.example.danielpark2.riotapp.DB.RiotDBOpenHelper;
import com.example.danielpark2.riotapp.Data.Region;
import com.example.danielpark2.riotapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by daniel.park2 on 1/29/15.
 */


public class APIService extends Service {

    private static final int MINUTE_MILLIS = 60000;
    private static final int POLLING_INTERVAL = MINUTE_MILLIS * 10; //updating every 10 minutes.
    public static HashMap<Integer, String> incidentCache;
    public static SQLiteDatabase sqlDBReadble;
    private static String TAG = "API service";
    private static SQLiteOpenHelper dbhelper;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    private SQLiteDatabase sqlDB;

    public static SQLiteDatabase getSqlDBReadble() {

        if (sqlDBReadble == null) {
            sqlDBReadble = dbhelper.getReadableDatabase();
        }
        return sqlDBReadble;
    }

    @Override
    public void onCreate() {
        //initial call
        super.onCreate();

        //init db
        dbhelper = new RiotDBOpenHelper(this);
        sqlDBReadble = dbhelper.getReadableDatabase();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String defaultRegion = (String) intent.getExtras().get("defaultRegion");


        //then schedule alarm
        //scheduleAlarm(30000); // 30 seconds

        incidentCache = new HashMap<Integer, String>();
        Log.i("i", "init db being called");
        initDB();
        //readDB();

        //oneTimeAPICall(defaultRegion);
        Log.i(TAG, "launching inital api call");

        //call get all shards
        Intent getAllShardsIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        getAllShardsIntent.putExtra("region", "shards");
        sendBroadcast(getAllShardsIntent);

        //default region
        Intent initIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        initIntent.putExtra("region", defaultRegion);
        sendBroadcast(initIntent);


        //TODO: schedule alarm for all 9 regions
        //just schedule once but with all the strings

        scheduleAlarm(POLLING_INTERVAL, Region.allRegionsList);


        //run service until it is explicitly stopped
        return START_STICKY;
    }


    public void scheduleAlarm(int intervalMillis, ArrayList<String> allRegionsList) {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("region", "allRegions");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervalMillis, alarmIntent);

        //readDB();

    }

    //without putting to intent extra
    public void scheduleAlarm(int intervalMillis) {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervalMillis, alarmIntent);


    }


    private void initDB() {

        sqlDB = dbhelper.getWritableDatabase();
        sqlDBReadble = dbhelper.getReadableDatabase();

        //init all the region columns
        Log.i("i", "initing the initial region names");
        for (String reg : Region.allRegions) {
            ContentValues values = new ContentValues();
            values.put(RiotDBOpenHelper.COLUMN_REGION, reg);
            sqlDB.insert(RiotDBOpenHelper.TABLE_API, null, values);
        }

    }

    private void readDB() {

        String[] allColumns = {RiotDBOpenHelper.COLUMN_ID,
                RiotDBOpenHelper.COLUMN_REGION};

        SQLiteDatabase readableDB = dbhelper.getReadableDatabase();
        Cursor cursor = readableDB.query(RiotDBOpenHelper.TABLE_API, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Log.i("0 th column", cursor.getString(0));
            Log.i("1st column", cursor.getString(1));

            cursor.moveToNext();

        }
        cursor.close();
        readableDB.close();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

