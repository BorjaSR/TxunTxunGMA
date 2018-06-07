package es.bsalazar.txuntxungma.utils;

import android.util.Log;

/**
 * Created by borja.salazar on 21/03/2018.
 */

public class LogUtils {

    public static void log(String title, String msg){
        Log.d(title, msg);
    }
    public static void logError(String title, String msg){
        Log.e(title, msg);
    }
}
