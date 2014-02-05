package com.easyplaylist.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Makar on 2/4/14.
 */
public class StringUtils {
    public static String convertMsToTimeFormat(String timeInMs) {
        Long timeInMsLong = Long.valueOf(timeInMs);
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMsLong);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMsLong - TimeUnit.MINUTES.toMillis(minutes));
        return minutes.toString()+":"+(seconds > 9 ? seconds.toString() : "0"+seconds.toString());
    }

    public static void main(String[] args) {
        String time = "128000";
        System.out.println(convertMsToTimeFormat("128000"));
        System.out.println(convertMsToTimeFormat("129000"));
        System.out.println(convertMsToTimeFormat("130000"));
        System.out.println(convertMsToTimeFormat("132000"));
    }

}
