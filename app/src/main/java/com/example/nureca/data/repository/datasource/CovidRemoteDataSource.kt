package com.example.nureca.data.repository.datasource

import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.model.CountryStatisticsItem
import retrofit2.Response

interface CovidRemoteDataSource {
    suspend fun getCountriesFromAPI(): Response<List<CountryItem>>
    suspend fun getCountryStatisticsFromAPI(countryName : String) : Response<List<CountryStatisticsItem>>
}