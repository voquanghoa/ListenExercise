package com.lovelybroteam.listenexercise.util;

import android.util.Log;

/**
 * Created by Vo Quang Hoa on 20/09/2015.
 */
public class Utils {
    private static String TAG = "ENGLISH_PUZZLE";
    private static String TAG_ERROR = "ENGLISH_PUZZLE_ERROR";

    private static void Log(String content, String tag){
        if(content==null){
            content = "(Empty log)";
        }
        Log.i(tag,content);
    }

    public  static void Log(String content){
        Log(content, TAG);
    }

    public static void Log(Exception exception){
        Log(exception.getMessage(),TAG_ERROR);
        exception.printStackTrace();
    }

    private static int adsCheck = 0;
    private static final int ADS_SHOW_RATIO = 3;
    public static boolean checkAds(){
        return (++adsCheck) % ADS_SHOW_RATIO == 0;
    }

    public static int limit(int value, int minimal, int maximum){
        if(value < minimal){
            return minimal;
        }

        if(value > maximum){
            return maximum;
        }
        return  value;
    }
}
