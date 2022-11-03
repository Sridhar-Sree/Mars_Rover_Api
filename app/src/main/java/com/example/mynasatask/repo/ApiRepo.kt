package com.example.mynasatask.repo

import android.util.Log
import com.example.mynasatask.model.Photo
import com.example.mynasatask.ui.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
 class MarsRepo @Inject constructor(
    private val api: com.example.mynasatask.network.MarsApi
) {

    suspend fun getMarsList(): Resource<com.example.mynasatask.model.MarsApi> {
        val response = try {
            api.getMarsListApi()
        } catch (e: Exception) {
            Log.d("No Reponse", "getMarsList: $e")
            return Resource.Error("An unknown error occured.")

        }
        Log.d("My Reponse", "getMarsList: $response")
        return Resource.Success(response)
    }

    suspend fun getMarsPhotoInfo(Photoid: Int): Resource<Photo> {
        val response = try {
            api.getPhoto(Photoid)
        } catch (e: Exception) {
            Log.d("Error Details Reponse", "getMarsList: $e")
            return Resource.Error("An unknown error occured.")
        }
        Log.d("Success Details Reponse", "getMarsList: $response")
        return Resource.Success(response)
    }


}