package com.example.nureca.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.domain.usecase.GetCountryStatisticsUseCase

class CountryStatisticsViewModelFactory (
    private val app: Application,
    private val getCountryStatisticsUseCase: GetCountryStatisticsUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountryStatisticsViewModel(
            app,
            getCountryStatisticsUseCase
        ) as T
    }
}