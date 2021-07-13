package com.example.nureca.presentation.di

import com.example.nureca.domain.repository.CovidRepository
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.domain.usecase.GetCountryStatisticsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CountryStatisticsUseCaseModule {
    @Singleton
    @Provides
    fun provideCountriesStatisticsUseCase(covidRepository: CovidRepository): GetCountryStatisticsUseCase {
        return GetCountryStatisticsUseCase(covidRepository)
    }
}