package com.tonytang.demo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tonytang.demo.retrofit.service.CacheConfigInterceptor;
import com.tonytang.demo.ui.activity.AndroidApplication;

/**
 * Created by tonythompson on 3/9/16.
 */
public class AppUtils {

    /**
     * check the network is connected or not.
     * <p>
     * Warning: This might not be the best approach to inquiry the network status,
     * especially this method will be frequently called in every network request triggered by {@link CacheConfigInterceptor}.
     *
     * @return true if the device is connected to network or false otherwise.
     */
    public static boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) AndroidApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


}
