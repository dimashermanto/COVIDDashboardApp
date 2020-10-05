package com.example.kawalcovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kawalcovid.R
import com.example.kawalcovid.dataClasses.GlobalOverallResponse
import com.example.kawalcovid.dataClasses.ProvinceResponse
import kotlinx.android.synthetic.main.item_province.view.*
import java.text.NumberFormat

class ProvinceAdapter(private var list: ArrayList<ProvinceResponse>, private val itemClickListener: ProvinceListOnItemClickListener): RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder>() {

    inner class ProvinceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(province: ProvinceResponse, clickListener: ProvinceListOnItemClickListener){
            with(itemView){
                tvProvinceName.text = province.attributes.province
                tvPositive.text = NumberFormat.getInstance().format(province.attributes.positive).toString()
                tvDeath.text = NumberFormat.getInstance().format(province.attributes.death).toString()
            }

            itemView.setOnClickListener {
                clickListener.onItemClickListener(province)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_province, parent, false)
        return ProvinceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProvinceViewHolder, position: Int) {
        holder.bind(list[position], itemClickListener)
    }

    fun filterList(filteredList: ArrayList<ProvinceResponse>) {
        list = filteredList
        notifyDataSetChanged()
    }
}

interface ProvinceListOnItemClickListener{
    fun onItemClickListener(item: ProvinceResponse)
}