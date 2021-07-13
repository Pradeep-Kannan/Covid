package com.example.nureca.data.repository

import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.model.CountryStatisticsItem
import com.example.nureca.data.repository.datasource.CovidRemoteDataSource
import com.example.nureca.data.util.Resource
import com.example.nureca.domain.repository.CovidRepository
import retrofit2.Response

class CovidRepositoryImpl(private val covidRemoteDataSource: CovidRemoteDataSource) :
    CovidRepository {

    override suspend fun getCountries(): Resource<List<CountryItem>> {
        return responseToResource(covidRemoteDataSource.getCountriesFromAPI())
    }

    private fun responseToResource(response: Response<List<CountryItem>>): Resource<List<CountryItem>> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getCountryStatistics(countryName : String): Resource<List<CountryStatisticsItem>> {
        return responseToResource2(covidRemoteDataSource.getCountryStatisticsFromAPI(countryName))
    }

    private fun responseToResource2(response: Response<List<CountryStatisticsItem>>): Resource<List<CountryStatisticsItem>> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun saveCountry(countryItem: CountryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun saveCountryStatistics(countryStatisticsItem: CountryStatisticsItem) {
        TODO("Not yet implemented")
    }

    override fun getSavedCountries(): List<CountryItem> {
        TODO("Not yet implemented")
    }

    override fun getSavedCountryStatistics(): List<CountryStatisticsItem> {
        TODO("Not yet implemented")
    }
}