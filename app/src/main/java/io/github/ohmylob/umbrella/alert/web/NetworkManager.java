package io.github.ohmylob.umbrella.alert.web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import io.github.ohmylob.umbrella.alert.debug.Logger;

public class NetworkManager {

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void turnOnWiFi(Context context) {
        Logger.print("Turning on WiFi");

        ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
    }
}
