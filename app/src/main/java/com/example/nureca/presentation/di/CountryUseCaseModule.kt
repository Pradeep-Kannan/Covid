package com.example.nureca.presentation.di

import com.example.nureca.domain.repository.CovidRepository
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.domain.usecase.GetSavedCountriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CountryUseCaseModule {
    @Singleton
    @Provides
    fun provideCountriesUseCase(covidRepository: CovidRepository): GetCountriesUseCase {
        return GetCountriesUseCase(covidRepository)
    }
}