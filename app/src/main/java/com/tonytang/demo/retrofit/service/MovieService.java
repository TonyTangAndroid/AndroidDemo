package com.tonytang.demo.retrofit.service;

import com.tonytang.demo.model.MoviesWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MovieService {

	@GET("search/movie")
	Observable<MoviesWrapper> searchRx(@Query("api_key") String apiKey, @Query("query") String query);

}
