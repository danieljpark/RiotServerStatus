package com.example.danielpark2.riotapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by daniel.park2 on 1/31/15.
 */
public class RiotDBOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "api.db";
    public static final int DATABASE_VERSION = 1;


    public static final String TABLE_API = "apiTable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REGION = "region";
    public static final String COLUMN_DATE = "lastUpdated"; //dont have spaces in column names
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_INCIDENTS = "incidents";


    // Database creation sql statement
    private static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_API + " (" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_REGION + " TEXT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_STATUS + " TEXT, "
            + COLUMN_INCIDENTS + " TEXT "
            + ");";

    private static final String TAG = "DBHelper";


    public RiotDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_API);
        db.execSQL(TABLE_CREATE);
        Log.i(TAG, "Creating db table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_API);
        Log.i(TAG, "DB onUpgrade");
        onCreate(db); //recreated table
    }
}
