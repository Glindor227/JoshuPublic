package com.example.joshu.di.module

import com.example.joshu.BuildConfig.*
import com.example.joshu.mvp.model.api.IApiService
import com.example.joshu.mvp.model.api.network.error.NetworkParserError
import com.example.joshu.mvp.model.ISharedPreferences
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.CacheControl
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {
    companion object {
        private const val API_HEADER_KEY = "TOKEN AUTH"
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): IApiService {
        return retrofit.create(IApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkParserError(retrofit: Retrofit): NetworkParserError {
        return NetworkParserError(retrofit)
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {

        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferencesModel: ISharedPreferences,
                            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1

        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor { //cache
                    chain ->
                val response = chain.proceed(chain.request())
                // re-write response header to force use of cache
                val cacheControl = CacheControl.Builder()
                    .maxAge(2, TimeUnit.MINUTES)
                    .build()
                response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")

                val accessToken = sharedPreferencesModel.getAccessToken()

                accessToken?.let {
                    requestBuilder.header(API_HEADER_KEY, it)
                }

                val request = requestBuilder
                    .method(original.method, original.body)
                    .build()

                chain.proceed(request)
            }
            .dispatcher(dispatcher)

            .connectTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)

        if (DEBUG || ENABLE_LOG) {
            okHttpBuilder
                .addNetworkInterceptor(StethoInterceptor())
                .addNetworkInterceptor(httpLoggingInterceptor)
        }
        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }
}