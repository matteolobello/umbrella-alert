package io.github.ohmylob.umbrella.alert.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import io.github.ohmylob.umbrella.alert.R;
import io.github.ohmylob.umbrella.alert.alarm.AlarmSetter;
import io.github.ohmylob.umbrella.alert.debug.Log;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;
import io.github.ohmylob.umbrella.alert.weather.Weather;
import io.github.ohmylob.umbrella.alert.weather.WeatherChecker;
import io.github.ohmylob.umbrella.alert.weather.WeatherConditions;
import io.github.ohmylob.umbrella.alert.web.NetworkManager;

public class WeatherReceiver extends BroadcastReceiver {

    private static final String WIFI_FILTER = "android.net.conn.CONNECTIVITY_CHANGE";

    private static final int TEN_SECONDS = 10 * 1000;

    private Context context;

    private boolean isWiFiBroadcastReceiverRegistered;
    private boolean weatherChecked;

    private final BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.print("WiFiReceiver -> onReceive");

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                    && networkInfo.isConnected()) {
                Log.print("WiFi Connected");
                checkWeather();
            }
        }
    };

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;

        Log.print("WeatherReceiver -> onReceive");

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        String enableWiFiString = SharedPreferencesManager.getValue(context, SharedPreferencesManager.ENABLE_WIFI);
        if ((!enableWiFiString.equals("0") && Boolean.valueOf(enableWiFiString))
                && !networkInfo.isConnected()) {
            NetworkManager.turnOnWiFi(context);

            IntentFilter filter = new IntentFilter();
            filter.addAction(WIFI_FILTER);
            context.getApplicationContext().registerReceiver(wifiBroadcastReceiver, filter);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!weatherChecked) {
                        // force check weather if after 10 seconds we still haven't done it.
                        checkWeather();
                    }
                }
            }, TEN_SECONDS);

            isWiFiBroadcastReceiverRegistered = true;
        } else {
            checkWeather();
        }
    }

    private void checkWeather() {
        if (!weatherChecked) {
            if (NetworkManager.isNetworkAvailable(context)) {
                WeatherChecker.check(context, new WeatherChecker.OnLoadListener() {
                    @Override
                    public void onLoadComplete(Weather weather) {
                        if (weather != null) {
                            showNotification(weather);
                        }
                    }
                });

                String everyDayString = SharedPreferencesManager.getValue(context, SharedPreferencesManager.EVERY_DAY);

                if (!everyDayString.equals("0")) {
                    if (Boolean.valueOf(everyDayString)) {
                        AlarmSetter.getInstance(context).set();
                    } else {
                        SharedPreferencesManager.save(context, SharedPreferencesManager.IS_ALARM_SET, "false");
                        AlarmSetter.getInstance(context).destroyAlarms();
                    }
                }
            }
            if (isWiFiBroadcastReceiverRegistered) {
                context.getApplicationContext().unregisterReceiver(wifiBroadcastReceiver);
            }
            weatherChecked = true;
        }
    }

    private boolean checkRain(String conditions) {
        conditions = conditions.toLowerCase();

        return conditions.equals(WeatherConditions.RAIN)
                || conditions.equals(WeatherConditions.SHOWER_RAIN)
                || conditions.equals(WeatherConditions.THUNDERSTORM);
    }

    private void showNotification(Weather weather) {
        Log.print("Weather conditions -> " + weather.getConditions());

        if (checkRain(weather.getConditions())) {
            Log.print("Sending notification...");

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
            contentView.setTextViewText(R.id.temperature, weather.getCurrentTemperature() +
                    (Boolean.valueOf(SharedPreferencesManager.getValue(context, SharedPreferencesManager.USE_CELSIUS))
                            ? "°C"
                            : "°F"));
            contentView.setTextViewText(R.id.conditions, weather.getConditions());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.weather_notif)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.it_will_rain));
            Notification notification = builder.build();

            notification.bigContentView = contentView;

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(22, notification);
        }
    }
}
