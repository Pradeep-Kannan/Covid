package com.example.nureca.domain.usecase

import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.util.Resource
import com.example.nureca.domain.repository.CovidRepository

class GetCountriesUseCase(private val covidRepository: CovidRepository) {

    suspend fun execute(): Resource<List<CountryItem>> {
        return covidRepository.getCountries()
    }
}