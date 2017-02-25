package com.tonytang.demo.retrofit.service;

import android.support.annotation.NonNull;

import com.tonytang.demo.util.AppUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * This is an interceptor to config the cache of the http response.
 * Based on network status, the cache valid interval will be configured differently.
 * When the network is connected, its valid interval is only 10 seconds as we could always retrieve
 * the data from server.
 * When the network is disconnected, it will accept the cache response in the past month of such request.
 * <p>
 * <p>
 * You could test it out by searching a keyword. Then cut off all network including mobile network
 * and search the same keyword again. You will still get the result.
 * <p>
 * In this case, we do not have to manage cache by ourselves.
 */
public final class CacheConfigInterceptor implements Interceptor {

    public static final long CACHE_DURATION_WITH_NETWORK_IN_SECONDS = 10;//expired in 10 seconds.
    public static final long CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS = 7 * 24 * 60 * 60;//expired in once week.

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request compressedRequest = originalRequest.newBuilder()
                .header("Cache-Control", getCacheConfig()).build();
        return chain.proceed(compressedRequest);
    }

    @NonNull
    private String getCacheConfig() {
        return AppUtils.isConnected() ? getCacheConfigOnNetworkConnected() : getCacheConfigOnNetworkDisconnected();
    }


    @NonNull
    private String getCacheConfigOnNetworkDisconnected() {
        //hard code here as it is fixed settings.
        return "public, only-if-cached, max-stale=" + CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS;
    }

    @NonNull
    private String getCacheConfigOnNetworkConnected() {
        //hard code here as it is fixed settings.
        return "public, max-age=" + CACHE_DURATION_WITH_NETWORK_IN_SECONDS + ", max-stale=" + CACHE_DURATION_WITH_NETWORK_IN_SECONDS;
    }


}