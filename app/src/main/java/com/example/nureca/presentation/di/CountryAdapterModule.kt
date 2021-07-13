package com.example.nureca.presentation.di

import com.example.nureca.presentation.adapter.RealmCountryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CountryAdapterModule {

    @Singleton
    @Provides
    fun provideCountryAdapter():RealmCountryAdapter{
        return RealmCountryAdapter()
    }
}