package com.ilia_zusik.weatherapp.di

import com.ilia_zusik.weatherapp.BuildConfig
import com.ilia_zusik.weatherapp.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder()
                .addNetworkInterceptor(Interceptor {
                    val originalRequest = it.request()
                    val newHttpUrl = originalRequest.url.newBuilder()
                        .addQueryParameter("appid", BuildConfig.API_KEY)
                        .build()
                    val newRequest = originalRequest.newBuilder()
                        .url(newHttpUrl)
                        .build()
                    it.proceed(newRequest)})
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build())
            .build().create(WeatherApi::class.java)
    }
}