package com.example.hamid.movies.presentation.di.koin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hamid.data.remote.APIService
import com.hamid.domain.model.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val httpModule = module {

    single<Gson> {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.create()
    }

    single<Cache> {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(androidContext().cacheDir, "http-cache")
        Cache(httpCacheDirectory, cacheSize)
    }

    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.cache(get())
        httpClient.addInterceptor(logging)
        httpClient.writeTimeout(60, TimeUnit.MINUTES)
        httpClient.connectTimeout(60, TimeUnit.MINUTES)
        httpClient.readTimeout(60, TimeUnit.MINUTES)
        httpClient.build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(Constants.baseURL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<APIService> {
        provideApiService(get())
    }

}

fun provideApiService(retrofit: Retrofit): APIService {
    return retrofit.create(APIService::class.java)
}