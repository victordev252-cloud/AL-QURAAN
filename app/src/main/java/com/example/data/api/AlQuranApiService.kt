package com.example.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface AlQuranApiService {

    @GET("surah")
    suspend fun getAllSurahs(): AlQuranApiResponse<List<ApiSurahListItem>>

    @GET("surah/{surahNumber}/quran-uthmani")
    suspend fun getSurahArabic(
        @Path("surahNumber") surahNumber: Int
    ): AlQuranApiResponse<ApiSurahDetailData>

    @GET("surah/{surahNumber}/so.abduh")
    suspend fun getSurahSomali(
        @Path("surahNumber") surahNumber: Int
    ): AlQuranApiResponse<ApiSurahDetailData>

    @GET("surah/{surahNumber}/en.sahih")
    suspend fun getSurahEnglish(
        @Path("surahNumber") surahNumber: Int
    ): AlQuranApiResponse<ApiSurahDetailData>

    @GET("surah/{surahNumber}/ar.alafasy")
    suspend fun getSurahAlafasyAudio(
        @Path("surahNumber") surahNumber: Int
    ): AlQuranApiResponse<ApiSurahDetailData>

    @GET("surah/{surahNumber}/ar.abdulbasitmurattal")
    suspend fun getSurahAbdulbasitAudio(
        @Path("surahNumber") surahNumber: Int
    ): AlQuranApiResponse<ApiSurahDetailData>
}

object ApiClient {
    private const val BASE_URL = "https://api.alquran.cloud/v1/"

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val apiService: AlQuranApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AlQuranApiService::class.java)
    }
}
