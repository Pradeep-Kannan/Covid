package com.example.nureca.presentation.di

import com.example.nureca.BuildConfig
import com.example.nureca.data.api.CovidAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS).build()
            )
            .baseUrl(BuildConfig.BASE_URL).build()
    }

    @Singleton
    @Provides
    fun provideCovidApiService(retrofit: Retrofit): CovidAPIService {
        return retrofit.create(CovidAPIService::class.java)
    }
}