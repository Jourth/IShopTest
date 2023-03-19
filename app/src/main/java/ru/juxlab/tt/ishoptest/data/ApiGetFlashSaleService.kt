package ru.juxlab.tt.ishoptest.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiGetFlashSaleService {

    @GET("flash_sale.json")
    fun getFlashSaleProducts(): Deferred<FlashSaleList>

    companion object{
        operator fun invoke(): ApiGetFlashSaleService{
            return Retrofit.Builder()
                .client(
                    OkHttpClient()
                    .newBuilder()
                    .build())
                .baseUrl("https://run.mocky.io/v3/a9ceeb6e-416d-4352-bde6-2203416576ac/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiGetFlashSaleService::class.java)
        }
    }
}