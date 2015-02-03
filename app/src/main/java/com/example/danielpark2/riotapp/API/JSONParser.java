package com.example.danielpark2.riotapp.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class JSONParser {
    private static final String TAG = "jsonParser";


    // constructor
    public JSONParser() {
    }

    public static String parseLastUpdatedTime(JSONObject gameStatus) {
        JSONArray incidentsArr = null;
        JSONObject id = null;
        String createdAt = null;
        String res = null;
        try {
            incidentsArr = gameStatus.getJSONArray("incidents");
            id = incidentsArr.getJSONObject(0);
            createdAt = (String) id.get("created_at");
            if (id.getBoolean("active")) {
                res = friendlyTimeConverter(createdAt);
            }
        } catch (JSONException e) {
            res = systemTime();
        } catch (ParseException e) {
            res = systemTime();
        }

        return res;


    }

    public static String systemTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        String dayString = "" + day;
        if (day < 10) {
            dayString = "0" + day;
        }

        String hourString = "" + hour;
        if (hour < 10) {
            hourString = "0" + hour;
        }
        String minString = "" + min;
        if (min < 10) {
            minString = "0" + min;
        }

        return month + "/" + dayString + " " + hourString + " : " + minString;
    }


    public static String friendlyTimeConverter(String time) throws ParseException {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = df1.parse(time.replace("Z", "+0000"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        String dayString = "" + day;
        if (day < 10) {
            dayString = "0" + day;
        }

        String hourString = "" + hour;
        if (hour < 10) {
            hourString = "0" + hour;
        }
        String minString = "" + min;
        if (min < 10) {
            minString = "0" + min;
        }

        return month + "/" + dayString + " " + hourString + " : " + minString;

    }


    public static String parseIncidents(JSONObject gameStatus) throws JSONException {
        String results = "";
        //separated by delimiter colon
        JSONObject gObj = gameStatus.getJSONArray("incidents").getJSONObject(0);
        String content = (String) gObj.getJSONArray("updates").getJSONObject(0).get("content");
        if (content == null || content == "") {
            return "";
        } else {
            results += content;
        }
        return results;
    }


    public static String parseStatus(JSONObject gameStatus) throws JSONException {

        String status = gameStatus.getString("status");
        return status;
    }


}