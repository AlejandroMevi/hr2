package com.venturessoft.human.humanrhdapp.core

import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.venturessoft.human.humanrhdapp.network.URL
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitConnection(tipo: TypeServices) {
    private val retrofit: Retrofit

    init {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        var uRLTypeServices = ""
        when (tipo) {
            TypeServices.BASE -> {
                Log.i("RetrofitClient", "URL BASE: " + URL.URL_BASE)
                uRLTypeServices = URL.URL_BASE
            }

            TypeServices.KARDEX -> {
                Log.i("RetrofitClient", "URL KARDEX: " + URL.URL_KARDEX)
                uRLTypeServices = URL.URL_KARDEX
            }

            TypeServices.REPORTESAT -> {
                Log.i("RetrofitClient", "URL REPORTESAT: " + URL.URL_REPORTESAT)
                uRLTypeServices = URL.URL_REPORTESAT
            }
        }

        val builder = Retrofit.Builder()
            .baseUrl(uRLTypeServices)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS).build()
        retrofit = builder.client(okHttpClient).build()
    }

    fun getRetrofit(): Retrofit {
        return retrofit
    }
}