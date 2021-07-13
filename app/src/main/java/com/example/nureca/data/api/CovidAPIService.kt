package com.example.nureca.data.api

import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.model.CountryStatisticsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidAPIService {

    @GET("/countries")
    suspend fun getCountriesFromAPI(): Response<List<CountryItem>>

    @GET("/dayone/country/{countryname}")
    suspend fun getCountryStatisticsFromAPI(@Path("countryname") countryname: String): Response<List<CountryStatisticsItem>>

}