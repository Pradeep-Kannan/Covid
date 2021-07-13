package com.example.nureca.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.nureca.TAG
import com.example.nureca.data.model.CountryRealm
import com.example.nureca.domain.usecase.GetCountriesUseCase
import com.example.nureca.domain.usecase.GetSavedCountriesUseCase
import com.example.nureca.nurecaApp
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

class MainActivityViewModel(
    private val app: Application,
    private val getCountriesUseCase: GetCountriesUseCase
) : AndroidViewModel(app) {

    val countriesList: MutableLiveData<List<CountryRealm>> = MutableLiveData()


    fun getCountriesFromRealm(config: SyncConfiguration) {
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                readCountryData(realm)
            }

        })
    }

    private fun readCountryData(realm: Realm) {
        val country = realm.where(CountryRealm::class.java).sort("countryName").findAll()
        Log.i(TAG(), "readCountryData: $country")
        countriesList.postValue(country)
        val changeListener =
            OrderedRealmCollectionChangeListener<RealmResults<CountryRealm>> { results, changeSet ->
                Log.i(TAG(), "Country object initialized, displaying countries list. $results")
                countriesList.postValue(results)
            }
        country.addChangeListener(changeListener)

    }
}