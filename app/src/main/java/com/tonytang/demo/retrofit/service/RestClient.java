package com.tonytang.demo.retrofit.service;

import com.tonytang.demo.BuildConfig;
import com.tonytang.demo.constants.Constants;
import com.tonytang.demo.ui.activity.AndroidApplication;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String BASE_URL = Constants.MOVIE_DB_HOST;
    private static final String CACHE_DIRECTORY_RETROFIT = "cache_directory";
    private static final long CACHE_SIZE_RETROFIT = 1000 * 1024;
    private final MovieService movieService;

    public RestClient() {
        File httpCacheDirectory = new File(AndroidApplication.getInstance().getCacheDir(), CACHE_DIRECTORY_RETROFIT);
        Cache httpResponseCache = new Cache(httpCacheDirectory, CACHE_SIZE_RETROFIT);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BASIC : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new CacheConfigInterceptor());
        httpClient.cache(httpResponseCache);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        movieService = retrofit.create(MovieService.class);

    }

    public MovieService getMovieService() {
        return movieService;
    }


}