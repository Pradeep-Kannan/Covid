package com.example.nureca.presentation.di

import android.app.Application
import com.example.nureca.domain.usecase.GetCountryStatisticsUseCase
import com.example.nureca.presentation.viewmodel.CountryStatisticsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CountryStatisticsFactoryModule {

    @Singleton
    @Provides
    fun provideCountryStatisticsViewModel(
        application: Application,
        getCountryStatisticsUseCase: GetCountryStatisticsUseCase
    ): CountryStatisticsViewModelFactory {
        return CountryStatisticsViewModelFactory(application, getCountryStatisticsUseCase)
    }
}