package com.example.nureca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nureca.data.model.CountryItem
import com.example.nureca.databinding.ActivityMainBinding
import com.example.nureca.presentation.adapter.RealmCountryAdapter
import com.example.nureca.presentation.ui.AddCountryActivity
import com.example.nureca.presentation.ui.CountryStatisticsActivity
import com.example.nureca.presentation.viewmodel.MainActivityViewModel
import com.example.nureca.presentation.viewmodel.MainActivityViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    @Inject
    lateinit var realmCountryAdapter: RealmCountryAdapter

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(getString(R.string.countries))
        mainActivityViewModel =
            ViewModelProvider(
                this,
                mainActivityViewModelFactory
            ).get(MainActivityViewModel::class.java)

        initRecyclerView()

    }

    override fun onStart() {
        super.onStart()
        showProgressBar()
        nurecaApp.loginAsync(Credentials.anonymous()) {
            if (it.isSuccess) {
                Log.i(TAG(), "onStart: Login success")
                val user: User? = nurecaApp.currentUser();
                val config = SyncConfiguration.Builder(user, "public")
                    .build()
                getCountriesFromDB(config)
            } else {
                Log.i(TAG(), "onStart: Login Failure")
            }
        }

    }

    private fun getCountriesFromDB(config: SyncConfiguration) {
        mainActivityViewModel.getCountriesFromRealm(config)
        mainActivityViewModel.countriesList.observe(this, Observer {
            Log.i(TAG(), "$it")
            realmCountryAdapter.setAdapter(it)
            realmCountryAdapter.notifyDataSetChanged()
            hideProgressBar()
            if(it.isEmpty()) {
                showInfoMessage()
            }else{
                hideInfoMessage()
            }

        })
    }

    private fun showInfoMessage() {
        binding.rvCountry.visibility = View.INVISIBLE
        binding.noData.visibility = View.VISIBLE
        binding.noData.setText(getString(R.string.no_data_msg))
    }

    private fun hideInfoMessage() {
        binding.rvCountry.visibility = View.VISIBLE
        binding.noData.visibility = View.INVISIBLE
        binding.noData.setText("")
    }

    private fun initRecyclerView() {
        binding.rvCountry.apply {
            adapter = realmCountryAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        realmCountryAdapter.setOnItemClickListener {
            val countryItem = CountryItem(it.countryName.toString(),it.slug.toString(),it.ISO2.toString())
            val intent = Intent(this, CountryStatisticsActivity::class.java)
            val bundle = Bundle().apply {
                putSerializable(COUNTRY_EXTRAS_BUNDLE,countryItem)
            }
            intent.putExtras(bundle)
            intent.putExtra(SCREEN_NAME, "mainactivity")
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_country, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addCountryMenu -> {
                startActivity(Intent(this, AddCountryActivity::class.java))
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun showProgressBar() {
        binding.pgBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pgBar.visibility = View.INVISIBLE
    }
}