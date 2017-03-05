package com.example.allu.trackyourpal.GPS;

/**
 * Created by allu on 3/6/17.
 */

public class GPS_Util {
    static boolean GPS_Status_Flag;

    public static boolean isGPS_Status_Flag() {
        return GPS_Status_Flag;
    }

    public static void setGPS_Status_Flag(boolean GPS_Status_Flag) {
        GPS_Util.GPS_Status_Flag = GPS_Status_Flag;
    }
}
