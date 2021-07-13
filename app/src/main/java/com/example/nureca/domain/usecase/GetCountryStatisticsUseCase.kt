package com.example.nureca.domain.usecase

import com.example.nureca.data.model.CountryStatisticsItem
import com.example.nureca.data.util.Resource
import com.example.nureca.domain.repository.CovidRepository

class GetCountryStatisticsUseCase(private val covidRepository: CovidRepository) {

    suspend fun execute(countryName:String): Resource<List<CountryStatisticsItem>> {
        return covidRepository.getCountryStatistics(countryName)
    }
}