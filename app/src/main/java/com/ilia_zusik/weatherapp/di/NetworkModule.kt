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
object NetworkModule {

    @Provides
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideKeyInterceptor() = Interceptor {
        val originalRequest = it.request()
        val newHttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("appid", BuildConfig.API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()
        it.proceed(newRequest)
    }

    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        keyInterceptor: Interceptor
    ) =
        OkHttpClient().newBuilder()
            .addNetworkInterceptor(keyInterceptor)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .callTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
}
