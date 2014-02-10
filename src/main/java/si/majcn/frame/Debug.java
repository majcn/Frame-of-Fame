package si.majcn.frame;

import android.util.Log;

public class Debug {
    public static final boolean DEBUG = true;

    public static void printLog(String message, String tag) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void printLog(String message) {
        printLog(message, "Frame-of-Fame");
    }
}
