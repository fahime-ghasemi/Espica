package com.espica

import androidx.multidex.MultiDexApplication
import com.espica.data.network.NetworkApiService
import com.espica.data.network.Url
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import android.os.Build
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import io.fabric.sdk.android.services.settings.IconRequest.build
import io.fabric.sdk.android.services.settings.IconRequest.build




class EspicaApp: MultiDexApplication() {

    lateinit var networkApiService: NetworkApiService

    override fun onCreate() {
        super.onCreate()
        networkApiService = provideApiService()
        // Enabling database for resume support even after the application is killed:
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()

        PRDownloader.initialize(applicationContext, config)
    }

    internal fun provideClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }
    fun provideRetrofit(baseURL: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideApiService(): NetworkApiService {
        val stringBuilder = StringBuilder(Url.BASE_URL)

        return provideRetrofit(stringBuilder.toString(), provideClient()).create(NetworkApiService::class.java)
    }


}