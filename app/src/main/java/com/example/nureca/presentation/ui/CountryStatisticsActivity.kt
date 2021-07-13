package com.example.nureca.presentation.ui

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nureca.*
import com.example.nureca.data.model.CountryItem
import com.example.nureca.data.model.CountryRealm
import com.example.nureca.data.util.Resource
import com.example.nureca.databinding.ActivityCountryStatisticsBinding
import com.example.nureca.presentation.viewmodel.CountryStatisticsViewModel
import com.example.nureca.presentation.viewmodel.CountryStatisticsViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import dagger.hilt.android.AndroidEntryPoint
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import javax.inject.Inject


@AndroidEntryPoint
class CountryStatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountryStatisticsBinding
    private lateinit var countryStatisticsViewModel: CountryStatisticsViewModel

    @Inject
    lateinit var countryStatisticsViewModelFactory: CountryStatisticsViewModelFactory

    private var barChart: BarChart? = null
    private lateinit var dataSets: ArrayList<IBarDataSet>
    private lateinit var xAxisValues: ArrayList<String>
    private var screen: String? = null
    private lateinit var countryObject: CountryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countryObject = intent.extras?.getSerializable(COUNTRY_EXTRAS_BUNDLE) as CountryItem
        screen = intent.extras?.getString(SCREEN_NAME)
        title = countryObject.country
        Log.i(TAG(), "Country name:" + countryObject.country)

        countryStatisticsViewModel =
            ViewModelProvider(this, countryStatisticsViewModelFactory).get(
                CountryStatisticsViewModel::class.java
            )

        xAxisValues = ArrayList<String>()
        barChart = binding.chart
        barChart!!.setNoDataText("")
        countryStatisticsViewModel.yearsListMutable.observe(this, Observer {
            xAxisValues.addAll(it)
        })
        countryStatisticsViewModel.resultsListMutable.observe(this, Observer {
            drawChart(it)
        })
    }

    override fun onStart() {
        super.onStart()
        if(barChart?.data == null) {
            val user: User? = nurecaApp.currentUser();
            val config = SyncConfiguration.Builder(user, "public")
                .build()
            val countryRealm = CountryRealm().apply {
                countryName = countryObject.country
                slug = countryObject.slug
                ISO2 = countryObject.iSO2
            }
            if (!screen!!.contentEquals("mainactivity")) {
                countryStatisticsViewModel.saveCountryToRealm(config, countryRealm)
            }
            getCountrySlug(countryObject.country, countryObject.slug, screen, config)
        }

    }

    private fun getCountrySlug(
        country: String,
        countrySlug: String,
        screen: String?,
        config: SyncConfiguration
    ) {
        if (!screen!!.contentEquals("mainactivity")) {
            countryStatisticsViewModel.getCountriesStatistics(countrySlug)
            countryStatisticsViewModel.countriesStatisticsList.observe(this, { response ->
                when (response) {
                    is Resource.Success -> {
                        Log.i(TAG(), "Loading Completed.")
                        response.data?.let {
                            Log.i(TAG(), "${it}")
                            countryStatisticsViewModel.setChartData(it, country)
                        }
                        hideProgressBar()
                    }
                    is Resource.Loading -> {
                        Log.i(TAG(), "Loading...")
                        showProgressBar()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let {
                            Toast.makeText(this, "An error occurred : $it", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }

            })
        } else {
            countryStatisticsViewModel.getCountriesStatisticsFromRealm(country, config)
        }
    }


    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun drawChart(barEntries: ArrayList<List<BarEntry>>) {
        if (barEntries.get(0).isNotEmpty() || barEntries.get(1).isNotEmpty() || barEntries.get(2)
                .isNotEmpty() || barEntries.get(3).isNotEmpty()
        ) {
            dataSets = ArrayList(barEntries.size)
            val set1: BarDataSet = BarDataSet(barEntries.get(0), getString(R.string.confirmed))
            set1.setColor(resources.getColor(R.color.orange))
            set1.valueTextColor = resources.getColor(R.color.black)
            set1.valueTextSize = 6f
            val set2: BarDataSet = BarDataSet(barEntries.get(1), getString(R.string.deaths))
            set2.setColors(resources.getColor(R.color.red))
            set2.valueTextColor = resources.getColor(R.color.black)
            set2.valueTextSize = 6f
            val set3: BarDataSet = BarDataSet(barEntries.get(2), getString(R.string.recovered))
            set3.setColors(resources.getColor(R.color.green))
            set3.valueTextColor = resources.getColor(R.color.black)
            set3.valueTextSize = 6f
            val set4: BarDataSet = BarDataSet(barEntries.get(3), getString(R.string.active))
            set4.setColors(resources.getColor(R.color.blue))
            set4.valueTextColor = resources.getColor(R.color.black)
            set4.valueTextSize = 6f
            dataSets.add(set1)
            dataSets.add(set2)
            dataSets.add(set3)
            dataSets.add(set4)
            val data = BarData(dataSets)
            barChart?.data = data
            barChart!!.axisLeft.axisMinimum = 0f
            barChart!!.description.isEnabled = false
            barChart!!.axisRight.axisMinimum = 0f
            barChart!!.setDrawBarShadow(false)
            barChart!!.setDrawValueAboveBar(true)
            barChart!!.setMaxVisibleValueCount(10)
            barChart!!.setPinchZoom(false)
            barChart!!.setDrawGridBackground(false)
            val l = barChart!!.legend
            l.isWordWrapEnabled = false
            l.textSize = 12f
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.form = Legend.LegendForm.CIRCLE
            val xAxis = barChart!!.xAxis
            xAxis.granularity = 1f
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(false)
            xAxis.labelRotationAngle = -45f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisMaximum = 2f
            barChart!!.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
            val leftAxis = barChart!!.axisLeft
            leftAxis.removeAllLimitLines()
            leftAxis.typeface = Typeface.DEFAULT
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            leftAxis.textColor = Color.BLACK
            leftAxis.setDrawGridLines(false)
            barChart!!.axisRight.isEnabled = false
            setBarWidth(data)
            barChart!!.animateY(1000)
            barChart!!.invalidate()
        } else {
            val paint: Paint = barChart!!.getPaint(Chart.PAINT_INFO)
            paint.textSize = 40f
            barChart!!.setNoDataText(getString(R.string.no_data_available))
            barChart!!.invalidate()
        }
    }

    private fun setBarWidth(barData: BarData) {
        //val defaultBarWidth : Float
        if (dataSets.size > 1) {
            val barSpace = 0.02f
            val groupSpace = 0.3f
            val defaultBarWidth = (1 - groupSpace) / dataSets.size - barSpace
            if (defaultBarWidth >= 0) {
                barData.barWidth = defaultBarWidth
            }
            val groupCount = xAxisValues.size
            if (groupCount != -1) {
                barChart!!.xAxis.axisMinimum = 0f
                barChart!!.xAxis.axisMaximum =
                    0 + barChart!!.barData.getGroupWidth(groupSpace, barSpace) * groupCount
                barChart!!.xAxis.setCenterAxisLabels(true)
            }
            barChart!!.groupBars(0f, groupSpace, barSpace)
            barChart!!.invalidate()
        }
    }
}


