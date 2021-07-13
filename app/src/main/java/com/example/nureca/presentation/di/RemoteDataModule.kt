package com.example.nureca.presentation.di

import com.example.nureca.data.api.CovidAPIService
import com.example.nureca.data.repository.datasource.CovidRemoteDataSource
import com.example.nureca.data.repository.datasourceimpl.CovidRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideCovidRemoteDataSource(covidAPIService: CovidAPIService): CovidRemoteDataSource {
        return CovidRemoteDataSourceImpl(covidAPIService)
    }
}