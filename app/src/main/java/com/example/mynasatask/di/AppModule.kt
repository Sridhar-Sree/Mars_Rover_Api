package com.example.mynasatask.di

import com.example.mynasatask.network.MarsApi
import com.example.mynasatask.repo.MarsRepo
import com.example.mynasatask.ui.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMarsRepo(
        api: MarsApi
    ) = MarsRepo(api)

    @Singleton
    @Provides
    fun provideMarsApi(): MarsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MarsApi::class.java)
    }
}