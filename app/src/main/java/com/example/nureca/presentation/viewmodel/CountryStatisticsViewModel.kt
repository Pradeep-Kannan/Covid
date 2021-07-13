package com.example.nureca.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nureca.R
import com.example.nureca.TAG
import com.example.nureca.data.model.CountryRealm
import com.example.nureca.data.model.CountryStatistics
import com.example.nureca.data.model.CountryStatisticsItem
import com.example.nureca.data.model.CountryStatisticsRealm
import com.example.nureca.data.util.Resource
import com.example.nureca.domain.usecase.GetCountryStatisticsUseCase
import com.example.nureca.nurecaApp
import com.example.nureca.presentation.utility.Util
import com.github.mikephil.charting.data.BarEntry
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CountryStatisticsViewModel(
    private val app: Application,
    private val getCountryStatisticsUseCase: GetCountryStatisticsUseCase
) : AndroidViewModel(app) {

    val countriesStatisticsList: MutableLiveData<Resource<List<CountryStatisticsItem>>> =
        MutableLiveData()
    val resultsListMutable: MutableLiveData<ArrayList<List<BarEntry>>> = MutableLiveData()
    val yearsListMutable: MutableLiveData<List<String>> = MutableLiveData()
    private val yearsList = ArrayList<String>()
    private val resultsList = ArrayList<List<BarEntry>>()
    private val activeEntries = ArrayList<BarEntry>()
    private val recoveredEntries = ArrayList<BarEntry>()
    private val deathEntries = ArrayList<BarEntry>()
    private val confirmEntries = ArrayList<BarEntry>()

    fun getCountriesStatistics(countryName: String) = viewModelScope.launch(Dispatchers.IO) {
        countriesStatisticsList.postValue(Resource.Loading())
        try {
            if (Util.isNetworkAvailable(app)) {
                val apiResult = getCountryStatisticsUseCase.execute(countryName)
                countriesStatisticsList.postValue(apiResult)
            } else {
                countriesStatisticsList.postValue(Resource.Error(app.getString(R.string.internet_not_available)))
            }
        } catch (e: Exception) {
            countriesStatisticsList.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun setChartData(it: List<CountryStatisticsItem>, country: String?) {
        deleteCountryStatisticsFromRealm(country)
        val list = ArrayList<CountryStatistics>()
        it.forEach {
            val date = it.date
            val fullDateArr = date.split("T")
            val dateArr = fullDateArr[0].split("-")
            val countryStat =
                CountryStatistics(
                    dateArr[0].toInt(),
                    it.active,
                    it.recovered,
                    it.deaths,
                    it.confirmed
                )
            list.add(countryStat)
        }
        val distinctList = list.distinctBy { countryStat -> countryStat.date }
        Log.i(TAG(), "setChartData: disList : $distinctList")
        val yearList = ArrayList<List<CountryStatistics>>()
        distinctList.forEach {
            yearList.add(list.filter { countryStat -> countryStat.date == it.date })
            yearsList.add(it.date.toString())
        }
        yearsListMutable.value = yearsList
        var i = 0
        yearList.forEach {
            Log.i(TAG(), "it value = $it")
            i++;
            val active = it.map { it.active }.sum().toFloat()
            val recovered = it.map { it.recovered }.sum().toFloat()
            val deaths = it.map { it.deaths }.sum().toFloat()
            val confirmed = it.map { it.confirmed }.sum().toFloat()
            saveCountryStatisticsToRealm(
                country,
                it.get(i).date.toString(),
                active,
                recovered,
                deaths,
                confirmed
            )
            activeEntries.add(
                BarEntry(
                    i.toFloat(),
                    active
                )
            )
            recoveredEntries.add(
                BarEntry(
                    i.toFloat(),
                    recovered
                )
            )
            deathEntries.add(
                BarEntry(
                    i.toFloat(),
                    deaths
                )
            )
            confirmEntries.add(
                BarEntry(
                    i.toFloat(),
                    confirmed
                )
            )
        }
        resultsList.add(activeEntries)
        resultsList.add(recoveredEntries)
        resultsList.add(deathEntries)
        resultsList.add(confirmEntries)
        resultsListMutable.value = resultsList
        Log.i(TAG(), "setChartData: active : $activeEntries")
        Log.i(TAG(), "setChartData: recovered : $recoveredEntries")
        Log.i(TAG(), "setChartData: deaths : $deathEntries")
        Log.i(TAG(), "setChartData: confirmed : $confirmEntries")
    }

    private fun deleteCountryStatisticsFromRealm(country: String?) {
        val user: User? = nurecaApp.currentUser();
        val config = SyncConfiguration.Builder(user, "public")
            .build()
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                deleteCountryStatistics(realm, country)
            }
        })
    }

    private fun saveCountryStatisticsToRealm(
        countryName: String?,
        date: String,
        activeValue: Float,
        recoveredValue: Float,
        deathsValue: Float,
        confirmedValue: Float
    ) {
        val countryStatisticsRealm: CountryStatisticsRealm = CountryStatisticsRealm().apply {
            country = countryName
            year = date
            active = activeValue.toInt()
            recovered = recoveredValue.toInt()
            deaths = deathsValue.toInt()
            confirmed = confirmedValue.toInt()
        }
        val user: User? = nurecaApp.currentUser();
        val config = SyncConfiguration.Builder(user, "public")
            .build()
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                insertCountryStatistics(realm, countryStatisticsRealm)
            }
        })

    }

    private fun deleteCountryStatistics(
        realm: Realm,
        country: String?
    ) {
        realm.executeTransactionAsync { realm ->
            val result: RealmResults<CountryStatisticsRealm> =
                realm.where(CountryStatisticsRealm::class.java)
                    .equalTo("country", country).findAll()
            result.deleteAllFromRealm()
            Log.i(
                TAG(),
                "deleteCountryStatistics: Deleted successfully..${country}"
            )
        }
        realm.close()
    }

    private fun insertCountryStatistics(
        realm: Realm,
        countryStatisticsRealm: CountryStatisticsRealm
    ) {
        realm.executeTransactionAsync { realm ->
            realm.insert(countryStatisticsRealm)
            Log.i(
                TAG(),
                "insertCountryStatistics: Inserted successfully..${countryStatisticsRealm.country}"
            )
        }
        realm.close()
    }

    fun saveCountryToRealm(config: SyncConfiguration?, countryRealm: CountryRealm) {
        //val realm: Realm = Realm.getInstance(config!!)
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                insertCountry(realm, countryRealm)
            }
        })

    }

    private fun insertCountry(realm: Realm, countryRealm: CountryRealm) {
        realm.executeTransactionAsync { realm ->
            val result: RealmResults<CountryRealm> =
                realm.where(CountryRealm::class.java)
                    .equalTo("countryName", countryRealm.countryName).findAll()
            result.deleteAllFromRealm()
            realm.insert(countryRealm)
            Log.i(
                TAG(),
                "insertCountry: Inserted successfully..${countryRealm.countryName}"
            )
        }
        realm.close()
    }

    fun getCountriesStatisticsFromRealm(
        country: String,
        config: SyncConfiguration
    ) {
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                Log.i(TAG(), "getCountriesFromRealm()")
                readCountryStatisticsData(country, realm)
            }

        })
    }

    private fun readCountryStatisticsData(country: String?, realm: Realm) {
        val countryStat =
            realm.where<CountryStatisticsRealm>().equalTo("country", country).sort("year")
                .findAllAsync()
        val i = 0
        countryStat.forEach {
            Log.i(TAG(), "readCountryStatData:country Name : ${it.country}")
            activeEntries.add(
                BarEntry(
                    i.toFloat(),
                    it.active.toFloat()
                )
            )
            recoveredEntries.add(
                BarEntry(
                    i.toFloat(),
                    it.recovered.toFloat()
                )
            )
            deathEntries.add(
                BarEntry(
                    i.toFloat(),
                    it.deaths.toFloat()
                )
            )
            confirmEntries.add(
                BarEntry(
                    i.toFloat(),
                    it.confirmed.toFloat()
                )
            )
            yearsList.add(it.year.toString())
        }
        yearsListMutable.value = yearsList

        resultsList.add(activeEntries)
        resultsList.add(recoveredEntries)
        resultsList.add(deathEntries)
        resultsList.add(confirmEntries)
        resultsListMutable.value = resultsList
        Log.i(TAG(), "readCountryStatisticsData: active : $activeEntries")
        Log.i(TAG(), "readCountryStatisticsData: recovered : $recoveredEntries")
        Log.i(TAG(), "readCountryStatisticsData: deaths : $deathEntries")
        Log.i(TAG(), "readCountryStatisticsData: confirmed : $confirmEntries")
    }
}