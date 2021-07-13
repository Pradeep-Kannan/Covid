package com.example.nureca.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nureca.TAG
import com.example.nureca.data.model.CountryRealm
import com.example.nureca.databinding.ListCountryItemBinding

class RealmCountryAdapter : RecyclerView.Adapter<RealmCountryAdapter.CountryViewHolder>() {

    private val countryList = ArrayList<CountryRealm>()
    private lateinit var countryListFull: List<CountryRealm>

    fun setAdapter(list: List<CountryRealm>) {
        countryList.clear()
        countryList.addAll(list)
        countryListFull = ArrayList<CountryRealm>(countryList)
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
        fun bind(countryRealm: CountryRealm) {
            Log.i(TAG(), "country ${countryRealm.countryName}")
            binding.countryName.text = countryRealm.countryName
            binding.isoCode.text = countryRealm.slug

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(countryRealm)
                }
            }
        }
    }

    private var onItemClickListener: ((CountryRealm) -> Unit)? = null

    fun setOnItemClickListener(listener: (CountryRealm) -> Unit) {
        onItemClickListener = listener
    }

    /*override fun getFilter(): Filter {
        return filters;
    }

    private val filters = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filtertedList = ArrayList<CountryRealm>()
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
                    countryList.addAll(results.values as Collection<CountryRealm>)
                }
                notifyDataSetChanged()
            }
        }*/


}