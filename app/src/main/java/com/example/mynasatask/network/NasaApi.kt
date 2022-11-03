package com.example.mynasatask.network

import com.example.mynasatask.model.MarsApi
import com.example.mynasatask.model.Photo
import com.example.mynasatask.ui.Constants.DEMO_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsApi {

  //  https://api.nasa.gov/mars-photos/
    @GET("api/v1/rovers/curiosity/photos?sol=60&api_key=$DEMO_KEY")
    suspend fun getMarsListApi(): MarsApi

//    @GET("api/v1/rovers/curiosity/photos?sol=10&api_key=$DEMO_KEY")
//    suspend fun getNasaPhotos(): com.example.tasknasaapi.model.NasaApi

    @GET("api/v1/rovers/curiosity/photos?sol=10&api_key=$DEMO_KEY")
    suspend fun getPhoto(
        @Query("id") id: Int
    ): Photo
}