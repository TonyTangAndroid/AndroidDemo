package com.tonytang.demo.ui.activity;

import android.app.Application;

import com.tonytang.demo.retrofit.service.RestClient;

/**
 * Created by tonythompson on 3/8/16.
 */
public class AndroidApplication extends Application {


    private static AndroidApplication application;
    private RestClient restClient;

    public static AndroidApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        restClient = new RestClient();
    }

    public RestClient getRestClient() {
        return restClient;
    }


}
