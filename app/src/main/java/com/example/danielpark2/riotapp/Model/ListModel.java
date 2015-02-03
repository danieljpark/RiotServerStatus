package com.example.danielpark2.riotapp.Model;

/**
 * Created by daniel.park2 on 2/2/15.
 */
public class ListModel {

    private String regionFriendlyName = "";
    private String dateLastModified = "";
    private String status = "";

    /**
     * ******** Get Methods ***************
     */
    public String getRegionFriendlyName() {
        return this.regionFriendlyName;
    }

    /**
     * ******** Set Methods *****************
     */

    public void setRegionFriendlyName(String fname) {
        this.regionFriendlyName = fname;
    }

    public String getDateLastModified() {
        return this.dateLastModified;
    }

    public void setDateLastModified(String date) {
        this.dateLastModified = date;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String inStatus) {
        this.status = inStatus;
    }

}
