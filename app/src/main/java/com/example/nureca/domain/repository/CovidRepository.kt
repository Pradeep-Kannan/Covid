package com.example.nureca.domain.repository

import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.model.CountryStatisticsItem
import com.example.nureca.data.util.Resource

interface CovidRepository {

    suspend fun getCountries(): Resource<List<CountryItem>>
    suspend fun getCountryStatistics(countryName:String): Resource<List<CountryStatisticsItem>>
    suspend fun saveCountry(countryItem: CountryItem)
    suspend fun saveCountryStatistics(countryStatisticsItem: CountryStatisticsItem)
    fun getSavedCountries(): List<CountryItem> // TODO: 26-06-2021  realm
    fun getSavedCountryStatistics(): List<CountryStatisticsItem>// TODO: 26-06-2021  realm

}