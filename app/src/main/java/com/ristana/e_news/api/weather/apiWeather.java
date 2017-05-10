package com.ristana.e_news.api.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.ristana.e_news.entity.weather.weatherResponse;
import com.ristana.e_news.entity.weather.weatherResponseCurrent;

/**
 * Created by hsn on 14/02/2017.
 */

public interface apiWeather {
    @GET("forecast/daily")
    Call<weatherResponse> getFiveDayWeather(@Query("lat") String lat,@Query("lon") String lon,@Query("mode") String mode,@Query("appid") String appid,@Query("units") String units);
    @GET("weather")
    Call<weatherResponseCurrent> getCurrentWeather(@Query("lat") String lat,@Query("lon") String lon,@Query("mode") String mode,@Query("appid") String appid,@Query("units") String units);


}

