package io.github.ohmylob.umbrella.alert.weather;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import io.github.ohmylob.umbrella.alert.debug.Log;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;
import io.github.ohmylob.umbrella.alert.weather.model.WeatherModel;
import io.github.ohmylob.umbrella.alert.web.ServerConnection;

public class WeatherChecker {

    private static final String APP_ID = "cafa95fc8fcdfdb31ffddf1f398205dc";

    public static void check(final Context context, final OnLoadListener onLoadListener) {
        new AsyncTask<String, String, String>() {

            private Weather weather;

            @Override
            protected String doInBackground(String... strings) {
                String url = "http://api.openweathermap.org/data/2.5/weather"
                        + "?lat=" + getLatitude(context)
                        + "&lon=" + getLongitude(context)
                        + "&APPID=" + APP_ID
                        + "&cnt=1"
                        + "&units=" + (useCelsius(context) ? "metric" : "imperial");

                Log.print(url);

                try {
                    String json = ServerConnection.getUrlContent(url);

                    WeatherModel weatherModel = new Gson().fromJson(json, WeatherModel.class);

                    String conditions = weatherModel.getWeather().get(0).getMain();

                    String temp = String.valueOf(weatherModel.getMain().getTemp());
                    String min = String.valueOf(weatherModel.getMain().getTempMin());
                    String max = String.valueOf(weatherModel.getMain().getTempMax());

                    weather = new Weather(conditions, temp, min, max);
                } catch (Exception exception) {
                    Log.print("Error checking weather -> " + exception.getLocalizedMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                onLoadListener.onLoadComplete(weather);
            }
        }.execute(null, null, null);
    }

    private static boolean useCelsius(Context context) {
        return Boolean.valueOf(SharedPreferencesManager.getValue(context,
                SharedPreferencesManager.USE_CELSIUS));
    }

    private static String getLatitude(Context context) {
        return SharedPreferencesManager.getValue(context,
                SharedPreferencesManager.LATITUDE);
    }

    private static String getLongitude(Context context) {
        return SharedPreferencesManager.getValue(context,
                SharedPreferencesManager.LONGITUDE);
    }

    public interface OnLoadListener {
        void onLoadComplete(Weather weather);
    }
}
