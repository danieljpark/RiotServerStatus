package com.example.danielpark2.riotapp.API;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.danielpark2.riotapp.Data.Region;

/**
 * Created by daniel.park2 on 1/29/15.
 */


public class AlarmReceiver extends BroadcastReceiver {

    private Context receiverContext;
    private String statusURL = "http://status.leagueoflegends.com/shards";

    @Override
    public void onReceive(Context context, Intent intent) {
        receiverContext = context;

        String checkingRegion = intent.getStringExtra("region");
        Log.i("RECEIVER", "received alarm and region is " + checkingRegion);
        if (checkingRegion.equals("all")) {
            getAllShards();
        } else {

            checkAllServers(Region.allRegions);
        }
    }

    private void checkAllServers(String[] serverArray) {

        for (String region : serverArray) {
            Log.i("Calling", "putting on executor check server status of " + region);
            APICallTask task = new APICallTask(receiverContext, region);
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusURL + "/" + region, APICallTask.REGION_STATUS_REQUEST);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                task.execute(statusURL + "/" + region, APICallTask.REGION_STATUS_REQUEST);
            }
        }
    }

    public void checkServerStatus(String region) {
        Log.i("Calling", "Calling check server status of " + region);
        APICallTask task = new APICallTask(receiverContext, region);
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusURL + "/" + region, APICallTask.REGION_STATUS_REQUEST);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            task.execute(statusURL + "/" + region, APICallTask.REGION_STATUS_REQUEST);
        }


    }

    public void getAllShards() {
        new APICallTask(receiverContext, "").execute(statusURL, APICallTask.GET_ALL_SHARDS_REQUEST);
    }


}