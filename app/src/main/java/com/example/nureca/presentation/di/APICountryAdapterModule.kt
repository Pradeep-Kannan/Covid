package com.example.nureca.presentation.di

import com.example.nureca.presentation.adapter.APICountryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APICountryAdapterModule {

    @Singleton
    @Provides
    fun provideAPICountryAdapter():APICountryAdapter{
        return APICountryAdapter()
    }
}