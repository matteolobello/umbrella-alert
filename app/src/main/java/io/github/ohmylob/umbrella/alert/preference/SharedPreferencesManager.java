package io.github.ohmylob.umbrella.alert.preference;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.ohmylob.umbrella.alert.debug.Log;

public class SharedPreferencesManager {

    public static final String EVERY_DAY = "everyday";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String CITY = "city";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String IS_ALARM_SET = "is_alarm_set";
    public static final String USE_CELSIUS = "use_celsius";
    public static final String ENABLE_WIFI = "enable_wifi";

    public static HashMap<String, String> buildHashMap(String[] keys, String[] values) {
        if (keys.length != values.length) {
            try {
                throw new Exception("Keys and values arrays must have the SAME length!");
            } catch (Exception ignored) {
            }
        }

        HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i < keys.length; i++) {
            Log.print(keys[i] + " -> " + values[i]);
            hashMap.put(keys[i], values[i]);
        }

        return hashMap;
    }

    public static void save(Context context, HashMap<String, String> hashMap) {
        Set set = hashMap.entrySet();
        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            save(context, String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
    }

    public static void save(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(key, value)
                .apply();
    }

    public static String getValue(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "0");
    }
}
