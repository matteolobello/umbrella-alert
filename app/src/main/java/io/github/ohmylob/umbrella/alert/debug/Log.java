package io.github.ohmylob.umbrella.alert.debug;

public class Log {
    // Set this boolean to false to disable logs
    private static final boolean DEBUG = true;

    private static final String TAG = "Umbrella Alert";

    public static void print(Object object) {
        if (DEBUG) android.util.Log.d(TAG, String.valueOf(object));
    }
}
