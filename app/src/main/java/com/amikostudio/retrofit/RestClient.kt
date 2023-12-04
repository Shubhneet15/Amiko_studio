package com.amikostudio.retrofit

import android.os.Build
import androidx.annotation.RequiresApi
import com.amikostudio.fragments.ApplicationGlobal
import com.amikostudio.fragments.Constants.Companion.API_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var REST_CLIENT: ApiInterface


        var gson = GsonBuilder()
            .setLenient()
            .create()
        @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
        fun get(): ApiInterface {
            retrofit = Retrofit.Builder().baseUrl("https://animate.cloudnex.in/api/").client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            REST_CLIENT = retrofit.create(ApiInterface::class.java)
            return REST_CLIENT


        }
        @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
        fun getOkHttpClient(): OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(5, TimeUnit.MINUTES)
            builder.readTimeout(5, TimeUnit.MINUTES)
            builder.writeTimeout(5, TimeUnit.MINUTES)
            builder.addNetworkInterceptor(httpLoggingInterceptor)
            builder.protocols(listOf(Protocol.HTTP_1_1))
            builder.addInterceptor { chain ->
                val request = chain.request()
                val header = request.newBuilder()?.header("Authorization",
                    "Bearer ${ApplicationGlobal.sessionId}")
                val build = header!!.build()
                chain.proceed(build)
            }
            return builder.build()
        }
        fun getRetrofitInstance(): Retrofit {
            return retrofit
        }
    }
}