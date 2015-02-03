package com.example.danielpark2.riotapp.API;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.example.danielpark2.riotapp.DB.RiotDBOpenHelper;
import com.example.danielpark2.riotapp.Data.Region;
import com.example.danielpark2.riotapp.MainActivity;
import com.example.danielpark2.riotapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by daniel.park2 on 1/29/15.
 */

//commit the data to sql

public class APICallTask extends AsyncTask<Object, Void, String> {

    public static final int GET_ALL_SHARDS_REQUEST = 0;
    public static final int REGION_STATUS_REQUEST = 1;
    public static final int REGION_COUNT = 9; //took out korea
    private static final String TAG = "apicall";
    private int requestType = 0;

    private Context taskContext;
    private NotificationManager notificationManager;
    private String region;

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    public APICallTask(Context context, String region) {
        //when we reach the api call task init, we have already fleshed out the apitable with the 10 regions names under column.regions
        taskContext = context;
        this.region = region;
        notificationManager = (NotificationManager) taskContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Object... params) {
        Log.i("i", "firing off an API request");
        requestType = (int) (params[1]);
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet((String) params[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        if (responseString != null) Log.i("i", "response string is" + responseString);
        return responseString;
    }


    @Override
    protected void onPostExecute(String result) {


        if (result == null) return;

        Log.i("i", "the request type is " + requestType);
        switch (requestType) {

            case GET_ALL_SHARDS_REQUEST:
                //TODO: PROCESS data  different by updating the current shards
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("i", "THIS IS JSON ARRAY LEN " + jsonArray.length());
                if (jsonArray.length() != REGION_COUNT) {
                    Log.i("i", "The number of regions have changed");
                }
                break;
            //region request
            case REGION_STATUS_REQUEST:
                //send notification
                Log.i(TAG, "here");

                //String status = parseStatus();
                String status = null;
                String lastUpdatedTime = null;
                String incidents = null;

                JSONObject regJSON = null;
                JSONObject gameStatus = null;
                try {
                    regJSON = new JSONObject(result);
                    gameStatus = (JSONObject) regJSON.getJSONArray("services").get(1);
                    Log.i("i", "game status json object is " + gameStatus);
                    status = JSONParser.parseStatus(gameStatus);
                    lastUpdatedTime = JSONParser.parseLastUpdatedTime(gameStatus);

                    incidents = JSONParser.parseIncidents(gameStatus);
                    Log.i("i", "this is incidents for " + region + " " + incidents);

                    Log.i(TAG, "this is the updated time for " + region + lastUpdatedTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (region.equals(MainActivity.homeActivityDefaultRegion)) {
                    constructNotification(region, status);
                }

                updateValueDB(region, status, lastUpdatedTime, incidents);
                break;
            default:
                Log.i(TAG, "reached default case");
                break;


        }
        return;
    }


    private void updateValueDB(String region, String status, String date, String incidents) {

        //WRITE TO DB
        dbhelper = new RiotDBOpenHelper(taskContext);
        database = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(RiotDBOpenHelper.COLUMN_STATUS, status);
        values.put(RiotDBOpenHelper.COLUMN_DATE, date);
        values.put(RiotDBOpenHelper.COLUMN_INCIDENTS, incidents);

        int rowId = Region.allRegionsList.indexOf(region) + 1; //sql rows begin from 1;

        database.update(RiotDBOpenHelper.TABLE_API, values, "id=" + rowId, null);

    }

    private void constructNotification(String region, String status) {

        Intent intent = new Intent(taskContext, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(taskContext, 0, intent, 0);


        Notification n = new Notification.Builder(taskContext)
                .setContentTitle("LoL Server Status")
                .setContentText(Region.getRegionFriendlyName(region) + " Server is " + status)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher, "And more", pIntent).build();

        notificationManager.notify(0, n);

    }


    private void checkForIncidents(JSONObject gameStatus) {


        //if inactive pull from hashmap and do some caching

        //TODO;
    }


}