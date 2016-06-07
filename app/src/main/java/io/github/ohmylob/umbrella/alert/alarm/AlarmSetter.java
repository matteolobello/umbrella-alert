package io.github.ohmylob.umbrella.alert.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import io.github.ohmylob.umbrella.alert.debug.Log;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;
import io.github.ohmylob.umbrella.alert.receiver.WeatherReceiver;

public class AlarmSetter {

    private final Context context;
    private final PendingIntent pendingIntent;
    private final AlarmManager alarmManager;

    private AlarmSetter(Context context) {
        this.context = context;
        this.pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, WeatherReceiver.class), 0);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static AlarmSetter getInstance(Context context) {
        return new AlarmSetter(context);
    }

    public void set() {
        Calendar calendar = Calendar.getInstance();

        String hourString = SharedPreferencesManager.getValue(context, "hour");
        String minuteString = SharedPreferencesManager.getValue(context, "minute");

        minuteString = minuteString.length() == 1
                ? "0" + minuteString
                : minuteString;

        int hour = Integer.valueOf(hourString);
        int minute = Integer.valueOf(minuteString);

        if (Integer.valueOf(hourString + minuteString)
                <= Integer.valueOf(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + String.valueOf(calendar.get(Calendar.MINUTE)))) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Log.print("Alarm set on " + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) + " at " + hour + ":" + minute);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        SharedPreferencesManager.save(context, SharedPreferencesManager.IS_ALARM_SET, "true");
    }

    public void destroyAlarms() {
        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            Log.print("Couldn't remove previous alarms -> " + e.toString());
        }
    }
}
