package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.getDateOfTodayStringFormatted
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {

    @GET("neo/rest/v1/feed")
    fun getAsteroidsDataAsync(
        @Query("start_date") startDate: String = getDateOfTodayStringFormatted(),
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Deferred<NetworkNearEarthObjectsContainer>

    @GET("planetary/apod")
    fun getNasaImageOfTheDayDataAsync(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Deferred<NetworkPictureOfDayContainer>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

class ConnectivityInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!WifiService.instance.isOnline()) {
            throw IOException("No internet connection")
        } else {
            return chain.proceed(chain.request())
        }
    }
}

object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(
            OkHttpClient
                .Builder()
                .addInterceptor(ConnectivityInterceptor())
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .build()

    val nasa: NasaService = retrofit.create(NasaService::class.java)
}