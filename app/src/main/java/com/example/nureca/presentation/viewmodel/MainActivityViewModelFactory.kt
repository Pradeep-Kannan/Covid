package com.example.nureca.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.domain.usecase.GetSavedCountriesUseCase

class MainActivityViewModelFactory (
    private val app: Application,
    private val getCountriesUseCase: GetCountriesUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            app,
            getCountriesUseCase
        ) as T
    }
}