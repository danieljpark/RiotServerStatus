package com.example.danielpark2.riotapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.danielpark2.riotapp.API.APIService;
import com.example.danielpark2.riotapp.DB.RiotDBOpenHelper;
import com.example.danielpark2.riotapp.Data.Region;
import com.example.danielpark2.riotapp.Model.CustomAdapter;
import com.example.danielpark2.riotapp.Model.ListModel;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    public static Context mContext;
    public static String homeActivityDefaultRegion = null;
    public ArrayList<ListModel> CustomListViewValuesArr;
    public ListView list;
    public CustomAdapter adapter;
    private Spinner spinner;
    private ListView listView;
    private String currentDefaultRegion;
    private SQLiteDatabase sqlReadable;
    private SQLiteOpenHelper rDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        //data struct setup

        Intent serviceIntent = new Intent(this, APIService.class);
        serviceIntent.putExtra("defaultRegion", "allRegions");
        startService(serviceIntent);

        startActivityForResult(new Intent(this, SettingsActivity.class), 2);

    }


    private void initListView() {

        Resources res = getResources();
        list = (ListView) findViewById(R.id.listView_feed);  // List defined in XML ( See Below )
        list.setAdapter(null);
        CustomListViewValuesArr = new ArrayList<ListModel>();
        /**************** Create Custom Adapter *********/
        adapter = new CustomAdapter(this, CustomListViewValuesArr, res);

        list.setAdapter(adapter);

        setListData();

    }


    public void setListData() {

        for (String region : Region.allRegions) {
            if (region.equals(homeActivityDefaultRegion)) {
                continue;
            }
            final ListModel model = new ListModel();

            /******* Firstly take data in model object ******/
            model.setRegionFriendlyName(Region.getRegionFriendlyName(region));

            String dbCheckDate = getDBCheckDate(region);
            if (dbCheckDate != null) {
                model.setDateLastModified(dbCheckDate);
            } else {
                model.setDateLastModified("" + System.currentTimeMillis());
            }

            String dbCheckStatus = getDBCheckStatus(region);
            if (dbCheckStatus != null) {
                model.setStatus(dbCheckStatus);
            } else {
                model.setStatus("n/a");
            }
            /******** Take Model Object in ArrayList **********/
            CustomListViewValuesArr.add(model);
        }

    }

    private String getDBCheckDate(String region) {

        Cursor cursor = APIService.getSqlDBReadble().query(RiotDBOpenHelper.TABLE_API,
                new String[]{RiotDBOpenHelper.COLUMN_ID, RiotDBOpenHelper.COLUMN_REGION,
                        RiotDBOpenHelper.COLUMN_DATE}, RiotDBOpenHelper.COLUMN_REGION + "=?",
                new String[]{region}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        Log.i(TAG, "dumping date cursor to string " + DatabaseUtils.dumpCursorToString(cursor));
        return "Last Updated: " + cursor.getString(2);
    }


    private String getDBCheckStatus(String region) {
        Cursor cursor = APIService.getSqlDBReadble().query(RiotDBOpenHelper.TABLE_API,
                new String[]{RiotDBOpenHelper.COLUMN_ID, RiotDBOpenHelper.COLUMN_REGION,
                        RiotDBOpenHelper.COLUMN_STATUS}, RiotDBOpenHelper.COLUMN_REGION + "=?",
                new String[]{region}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        Log.i(TAG, "dumping status cursor to string " + DatabaseUtils.dumpCursorToString(cursor));
        String stat = cursor.getString(2);
        stat = stat.substring(0, 1).toUpperCase() + stat.substring(1);
        return stat;
    }

    private String getDBIncidents(String region) {
        Cursor cursor = APIService.getSqlDBReadble().query(RiotDBOpenHelper.TABLE_API,
                new String[]{RiotDBOpenHelper.COLUMN_ID, RiotDBOpenHelper.COLUMN_REGION,
                        RiotDBOpenHelper.COLUMN_INCIDENTS}, RiotDBOpenHelper.COLUMN_REGION + "=?",
                new String[]{region}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        Log.i(TAG, "dumping incidents cursor to string " + DatabaseUtils.dumpCursorToString(cursor));
        return "Last Updated: " + cursor.getString(2);

    }

    private void displayDefaultRegion(String region) {

        TextView name = (TextView) findViewById(R.id.default_region_textview);
        name.setText(Region.getRegionFriendlyName(region));

        TextView lastUpdated = (TextView) findViewById(R.id.default_region_date_textview);
        lastUpdated.setText(getDBCheckDate(region));

        TextView status = (TextView) findViewById(R.id.default_region_status_textview);
        status.setText(getDBCheckStatus(region));

        LinearLayout defaultLL = (LinearLayout) findViewById(R.id.default_region_linearLayout);
        defaultLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inciIntent = new Intent(getApplicationContext(), IncidentActivity.class);
                inciIntent.putExtra("callingRegion", homeActivityDefaultRegion);
                String incidentsDB = getDBIncidents(homeActivityDefaultRegion);
                if (incidentsDB == null) {
                    incidentsDB = "none reported";
                }
                inciIntent.putExtra("incidents", incidentsDB);

                startActivity(inciIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        startActivityForResult(new Intent(this, SettingsActivity.class), 2);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //on selection from settings menu


        if (requestCode == 2) {
            if (data == null || data.getStringExtra("itemId") == null) {
                return;
            }

            int selectedRegionId = Integer.parseInt((data.getStringExtra("itemId")));
            String newRegion = Region.allRegions[selectedRegionId];
            currentDefaultRegion = newRegion;

            Log.i("i", "selected current region is " + currentDefaultRegion);
            homeActivityDefaultRegion = newRegion;
            displayDefaultRegion(newRegion);

            initListView();


        }


    }
}
