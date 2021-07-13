package com.example.nureca.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nureca.R
import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.util.Resource
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.presentation.utility.Util.Companion.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCountryViewModel(
    private val app: Application,
    private val getCountriesUseCase: GetCountriesUseCase
) : AndroidViewModel(app) {

    val countriesList: MutableLiveData<Resource<List<CountryItem>>> = MutableLiveData()

    fun getCountries() = viewModelScope.launch(Dispatchers.IO) {
        countriesList.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable(app)) {
                val apiResult = getCountriesUseCase.execute()
                countriesList.postValue(apiResult)
            } else {
                countriesList.postValue(Resource.Error(app.getString(R.string.internet_not_available)))
            }
        } catch (e: Exception) {
            countriesList.postValue(Resource.Error(e.message.toString()))
        }
    }
}