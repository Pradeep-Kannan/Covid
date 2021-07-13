package com.example.nureca.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.nureca.TAG
import com.example.nureca.data.model.CountryItem
import com.example.nureca.databinding.ListCountryItemBinding

class APICountryAdapter : RecyclerView.Adapter<APICountryAdapter.CountryViewHolder>(), Filterable {

    private val countryList = ArrayList<CountryItem>()
    private lateinit var countryListFull: List<CountryItem>

    fun setAdapter(list: List<CountryItem>) {
        countryList.clear()
        countryList.addAll(list)
        countryListFull = ArrayList<CountryItem>(countryList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            ListCountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount(): Int {
        return countryList.size
    }


    inner class CountryViewHolder(val binding: ListCountryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(countryItem: CountryItem) {
            Log.i(TAG(), "country ${countryItem.country}")
            binding.countryName.text = countryItem.country
            binding.isoCode.text = countryItem.iSO2

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(countryItem)
                }
            }
        }
    }

    private var onItemClickListener: ((CountryItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (CountryItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun getFilter(): Filter {
        return filters;
    }

    private val filters = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filtertedList = ArrayList<CountryItem>()
            if (constraint.toString().isEmpty()) {
                filtertedList.addAll(countryListFull)
            } else {
                for (listValue in countryListFull) {
                    if (listValue.country.toLowerCase()
                            .contains(constraint.toString().toLowerCase())
                    ) {
                        filtertedList.add(listValue)
                    }
                }
            }
            return FilterResults().apply {
                values = filtertedList
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if(results!=null) {
                countryList.clear()
                results.values?.let {
                    countryList.addAll(results.values as Collection<CountryItem>)
                }
                notifyDataSetChanged()
            }
        }

    }
}