package com.example.nureca.presentation.di

import com.example.nureca.data.repository.CovidRepositoryImpl
import com.example.nureca.data.repository.datasource.CovidRemoteDataSource
import com.example.nureca.domain.repository.CovidRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCovidRepository(covidRemoteDataSource: CovidRemoteDataSource): CovidRepository {
        return CovidRepositoryImpl(covidRemoteDataSource)
    }
}