package io.github.ohmylob.umbrella.alert.weather;

import android.content.Context;

import github.vatsal.easyweather.Helper.WeatherCallback;
import github.vatsal.easyweather.WeatherMap;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;

public class WeatherChecker {

    private static final String APP_ID = "cafa95fc8fcdfdb31ffddf1f398205dc";

    public static void check(final Context context, final WeatherCallback weatherCallback) {
        WeatherMap weatherMap = new WeatherMap(context, APP_ID);
        weatherMap.getLocationWeather(getLatitude(context), getLongitude(context), weatherCallback);
    }

    private static String getLatitude(Context context) {
        return SharedPreferencesManager.getValue(context,
                SharedPreferencesManager.LATITUDE);
    }

    private static String getLongitude(Context context) {
        return SharedPreferencesManager.getValue(context,
                SharedPreferencesManager.LONGITUDE);
    }
}
