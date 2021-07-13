package com.example.nureca

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

inline fun <reified T> T.TAG(): String = T::class.java.simpleName
lateinit var nurecaApp: App
const val COUNTRY_EXTRAS_BUNDLE = "COUNTRY"
/*const val COUNTRY_SLUG = "SLUG"
const val COUNTRY_ISO = "ISO"*/
const val SCREEN_NAME = "SCREEN_NAME"

@HiltAndroidApp
class CovidApp : Application(){
    override fun onCreate() {
        super.onCreate()
        // Initialize the Realm SDK
        Realm.init(this)
        nurecaApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .defaultSyncErrorHandler { _, error ->
                    Log.e(TAG(), "Sync error: ${error.errorMessage}")
                }
                .build())

        // Enable more logging in debug mode
        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ALL)
        }

        Log.v(TAG(), "Initialized the Realm App configuration for: ${nurecaApp.configuration.appId}")
    }
}