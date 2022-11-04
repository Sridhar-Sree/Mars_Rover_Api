package com.example.mynasatask.di

import com.example.mynasatask.BuildConfig
import com.example.mynasatask.network.MarsApi
import com.example.mynasatask.repo.MarsRepo
import com.example.mynasatask.ui.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideOkHttpLoggingInterceptor() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    @Singleton
    @Provides
    fun provideMarsApi(): MarsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(provideOkHttpLoggingInterceptor())
            .build()
            .create(MarsApi::class.java)
    }
}