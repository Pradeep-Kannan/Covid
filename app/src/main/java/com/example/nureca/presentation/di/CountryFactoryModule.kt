package com.example.nureca.presentation.di

import android.app.Application
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.domain.usecase.GetSavedCountriesUseCase
import com.example.nureca.presentation.viewmodel.MainActivityViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CountryFactoryModule {

    @Singleton
    @Provides
    fun provideCountryViewModel(
        application: Application,
        getCountriesUseCase: GetCountriesUseCase
    ): MainActivityViewModelFactory {
        return MainActivityViewModelFactory(application, getCountriesUseCase)
    }
}