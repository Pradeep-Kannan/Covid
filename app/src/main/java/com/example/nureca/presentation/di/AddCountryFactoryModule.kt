package com.example.nureca.presentation.di

import android.app.Application
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.presentation.viewmodel.AddCountryViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AddCountryFactoryModule {

    @Singleton
    @Provides
    fun provideAddCountryViewModel(
        application: Application,
        getCountriesUseCase: GetCountriesUseCase
    ): AddCountryViewModelFactory {
        return AddCountryViewModelFactory(application, getCountriesUseCase)
    }
}