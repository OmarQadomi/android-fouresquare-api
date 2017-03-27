package com.example.myplacesapp.myplacesapp.Api;

import com.example.myplacesapp.myplacesapp.DataModels.VenuesRespons;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("venues/search")
    Call<VenuesRespons> getNearbyVenues(@Query("ll") String ll, @Query("oauth_token") String token, @Query("v") String date);

    @POST("checkins/add")
    Call<VenuesRespons> postCheckIn( @Query("venueId") String venueId,@Query("ll") String ll, @Query("shout") String message);
}
