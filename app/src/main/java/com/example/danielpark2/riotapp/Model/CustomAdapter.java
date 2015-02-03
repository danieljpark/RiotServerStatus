package com.example.danielpark2.riotapp.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.danielpark2.riotapp.API.APIService;
import com.example.danielpark2.riotapp.DB.RiotDBOpenHelper;
import com.example.danielpark2.riotapp.Data.Region;
import com.example.danielpark2.riotapp.IncidentActivity;
import com.example.danielpark2.riotapp.MainActivity;
import com.example.danielpark2.riotapp.R;

import java.util.ArrayList;

/**
 * Created by daniel.park2 on 2/2/15.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    public Resources res;
    ListModel tempValues = null;
    int i = 0;
    /**
     * ******** Declare Used Variables ********
     */
    private Activity activity;
    private ArrayList data;

    /**
     * **********  CustomAdapter Constructor ****************
     */
    public CustomAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * *** Depends upon data size called for each row , Create each ListView row ****
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.regionName = (TextView) vi.findViewById(R.id.list_item_region_name);
            holder.lastMod = (TextView) vi.findViewById(R.id.list_item_region_last_updated);
            holder.status = (TextView) vi.findViewById(R.id.list_item_region_status);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.regionName.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (ListModel) data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.regionName.setText(tempValues.getRegionFriendlyName());
            holder.lastMod.setText(tempValues.getDateLastModified());
            holder.status.setText(tempValues.getStatus());


            /******** Set Item Click Listner for LayoutInflater for each row *******/
            //TODO: fix
            vi.setOnClickListener(new OnItemClickListener(tempValues.getRegionFriendlyName()));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    private String getDBIncidents(String region) {
        Cursor cursor = APIService.getSqlDBReadble().query(RiotDBOpenHelper.TABLE_API,
                new String[]{RiotDBOpenHelper.COLUMN_ID, RiotDBOpenHelper.COLUMN_REGION,
                        RiotDBOpenHelper.COLUMN_INCIDENTS}, RiotDBOpenHelper.COLUMN_REGION + "=?",
                new String[]{region}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }

        return "Last Updated: " + cursor.getString(2);

    }

    /**
     * ****** Create a holder Class to contain inflated xml file elements ********
     */
    public static class ViewHolder {

        public TextView regionName;
        public TextView lastMod;
        public TextView status;


    }

    /**
     * ****** Called when Item click in ListView ***********
     */
    private class OnItemClickListener implements View.OnClickListener {
        private String mRegion;

        OnItemClickListener(String inRegion) {


            mRegion = inRegion;
        }

        @Override
        public void onClick(View arg0) {

            Intent inciIntent = new Intent(MainActivity.mContext, IncidentActivity.class);
            //reverselookup region
            String reverseRegion = Region.reverseFNLookup(mRegion);
            inciIntent.putExtra("callingRegion", reverseRegion);
            String incidentsDB = getDBIncidents(reverseRegion);
            if (incidentsDB == null) {
                incidentsDB = "none reported";
            }
            inciIntent.putExtra("incidents", incidentsDB);

            activity.startActivity(inciIntent);
        }
    }

}
