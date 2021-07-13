package com.example.nureca.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nureca.COUNTRY_EXTRAS_BUNDLE
import com.example.nureca.R
import com.example.nureca.SCREEN_NAME
import com.example.nureca.TAG
import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.util.Resource
import com.example.nureca.databinding.ActivityAddCountryBinding
import com.example.nureca.presentation.adapter.APICountryAdapter
import com.example.nureca.presentation.viewmodel.AddCountryViewModel
import com.example.nureca.presentation.viewmodel.AddCountryViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddCountryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCountryBinding
    private lateinit var addCountryViewModel: AddCountryViewModel
    @Inject
    lateinit var addCountryViewModelFactory: AddCountryViewModelFactory


    @Inject
    lateinit var apiCountryAdapter: APICountryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.search_countries)
        addCountryViewModel =
            ViewModelProvider(this, addCountryViewModelFactory).get(AddCountryViewModel::class.java)

        initRecyclerView()
        getCountries()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu!!.findItem(R.id.searchIcon)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                apiCountryAdapter.filter.filter(newText)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun getCountries() {
        addCountryViewModel.getCountries()
        addCountryViewModel.countriesList.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    Log.i(TAG(), "Loading Completed.")
                    response.data?.let {
                        Log.i(TAG(), "${it}")
                        apiCountryAdapter.setAdapter(it.sortedWith(compareBy { it.country }))
                        apiCountryAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Loading -> {
                    Log.i(TAG(), "Loading...")
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(this, "An error occurred : $it", Toast.LENGTH_LONG).show()
                    }
                }
            }

        })
    }

    private fun initRecyclerView() {
        binding.rvAPICountry.apply {
            adapter = apiCountryAdapter
            layoutManager = LinearLayoutManager(this@AddCountryActivity)
        }

        apiCountryAdapter.setOnItemClickListener {
            val countryItem = CountryItem(it.country, it.slug, it.iSO2)
            val intent = Intent(this, CountryStatisticsActivity::class.java)
            val bundle = Bundle().apply {
                putSerializable(COUNTRY_EXTRAS_BUNDLE,countryItem)
            }
            intent.putExtras(bundle)
            intent.putExtra(SCREEN_NAME, "addcountry")
            startActivity(intent)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
}