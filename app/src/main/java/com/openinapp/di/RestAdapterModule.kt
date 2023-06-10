package com.openinapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.openinapp.network.NetworkApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RestAdapterModule {

    private val TAG: String = RestAdapterModule.javaClass.simpleName
    private const val DATE_FORMAT: String = "yyyy-MM-dd'T'HH:mm:ss"
    private const val TIME_OUT: Long = 60L
    private const val MAX_REQUEST_CALL: Int = 10
    private const val BASE_URL: String = "https://api.inopenapp.com/api/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        dispatcher: Dispatcher
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .dispatcher(dispatcher)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun getRestInstance(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    internal fun getNetworkInterfaceProvider(retrofit: Retrofit): NetworkApiInterface {
        return retrofit.create(NetworkApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setDateFormat(DATE_FORMAT).setLenient().create()

    @Provides
    @Singleton
    fun provideDispatcher(): Dispatcher = Dispatcher().apply { maxRequests = MAX_REQUEST_CALL }

}