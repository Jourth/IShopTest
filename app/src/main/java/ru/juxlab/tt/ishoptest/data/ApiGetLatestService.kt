package ru.juxlab.tt.ishoptest.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiGetLatestService {

    @GET("latest.json")
    fun getLatestProducts(): Deferred<LatestProductsList>

    companion object{
        operator fun invoke(): ApiGetLatestService{
            return Retrofit.Builder()
                .client(
                    OkHttpClient()
                    .newBuilder()
                    .build())
                .baseUrl("https://run.mocky.io/v3/cc0071a1-f06e-48fa-9e90-b1c2a61eaca7/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiGetLatestService::class.java)
        }
    }
}