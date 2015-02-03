package com.example.danielpark2.riotapp.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by daniel.park2 on 1/29/15.
 * Container class for regions
 */
public class Region {

    public static final String NORTH_AMERICA = "na";
    public static final String EU_WEST = "euw";
    public static final String EU_NORDIC_EAST = "eune";
    public static final String BRAZIL = "br";
    public static final String TURKEY = "tr";
    // public static final String KOREA = "kr"; //kr no api response
    public static final String RUSSIA = "ru";
    public static final String LATIN_AMERICA_NORTH = "lan";
    public static final String LATIN_AMERICA_SOUTH = "las";
    public static final String OCEANIA = "oce";


    public static final String[] allRegions = {BRAZIL, EU_NORDIC_EAST, EU_WEST, LATIN_AMERICA_NORTH, LATIN_AMERICA_SOUTH, NORTH_AMERICA, OCEANIA, RUSSIA, TURKEY};
    public static final ArrayList<String> allRegionsList = new ArrayList<>(Arrays.asList(allRegions));
    public static final HashMap<String, String> friendlyNames;
    public static final ArrayList<String> allRegionFriendlyNames = new ArrayList<>();

    static {
        friendlyNames = new HashMap<String, String>();

        friendlyNames.put(BRAZIL, "Brazil");
        friendlyNames.put(EU_NORDIC_EAST, "EU Nordic & East");
        friendlyNames.put(EU_WEST, "EU West");
        friendlyNames.put(LATIN_AMERICA_NORTH, "Latin America North");
        friendlyNames.put(LATIN_AMERICA_SOUTH, "Latin America South");
        friendlyNames.put(NORTH_AMERICA, "North America");
        friendlyNames.put(OCEANIA, "Oceania");
        friendlyNames.put(RUSSIA, "Russia");
        friendlyNames.put(TURKEY, "Turkey");
        //friendlyNames.put(KOREA, "Korea");

        for (String key : friendlyNames.keySet()) {
            allRegionFriendlyNames.add(friendlyNames.get(key));
        }

    }

    private String regionName;

    public Region(String name) {
        regionName = name;

    }

    public static String getRegionFriendlyName(String region) {
        return friendlyNames.get(region);
    }

    public static String reverseFNLookup(String region) {


        for (String key : friendlyNames.keySet()) {
            if (friendlyNames.get(key).equals(region)) {
                return key;
            }
        }
        return null;

    }

    public String getFriendlyName() {
        return friendlyNames.get(this.regionName);

    }


}
