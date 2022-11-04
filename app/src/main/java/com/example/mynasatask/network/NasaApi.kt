package com.example.mynasatask.network

import com.example.mynasatask.model.MarsApi
import com.example.mynasatask.model.Photo
import com.example.mynasatask.ui.Constants.DEMO_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsApi {

  //  https://api.nasa.gov/mars-photos/
    @GET("api/v1/rovers/curiosity/photos?sol=10&api_key=$DEMO_KEY")
    suspend fun getMarsListApi(): MarsApi

}