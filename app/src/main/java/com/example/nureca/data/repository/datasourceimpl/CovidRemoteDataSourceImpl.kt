package com.example.nureca.data.repository.datasourceimpl

import com.example.nureca.data.api.CovidAPIService
import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.model.CountryStatisticsItem
import com.example.nureca.data.repository.datasource.CovidRemoteDataSource
import retrofit2.Response

class CovidRemoteDataSourceImpl(private val covidAPIService: CovidAPIService) : CovidRemoteDataSource {

    override suspend fun getCountriesFromAPI(): Response<List<CountryItem>> {
        return covidAPIService.getCountriesFromAPI()
    }

    override suspend fun getCountryStatisticsFromAPI(countryName : String): Response<List<CountryStatisticsItem>> {
        return covidAPIService.getCountryStatisticsFromAPI(countryName)
    }
}