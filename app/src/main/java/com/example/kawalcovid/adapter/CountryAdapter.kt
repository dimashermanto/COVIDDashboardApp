package com.example.kawalcovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kawalcovid.R
import com.example.kawalcovid.dataClasses.Country
import com.example.kawalcovid.dataClasses.GlobalOverallResponse
import kotlinx.android.synthetic.main.item_country.view.*
import java.text.NumberFormat

class CountryAdapter(private var list: ArrayList<GlobalOverallResponse>, private var itemClickListener: CountryListOnItemClickListener): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(country: GlobalOverallResponse, clickListener: CountryListOnItemClickListener){
            with(itemView){
                tvCountryName.text = country.attributes.country
                tvCountryPositive.text = NumberFormat.getInstance().format(country.attributes.positive).toString()
                tvCountryDeath.text = NumberFormat.getInstance().format(country.attributes.death).toString()
            }

            itemView.setOnClickListener {
                clickListener.onItemClickListener(country)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(list[position], itemClickListener)
    }

    fun filterList(filteredList: ArrayList<GlobalOverallResponse>) {
        list = filteredList
        notifyDataSetChanged()
    }
}

interface CountryListOnItemClickListener{
    fun onItemClickListener(item: GlobalOverallResponse)
}