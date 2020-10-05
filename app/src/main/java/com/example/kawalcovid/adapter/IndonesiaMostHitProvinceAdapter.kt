package com.example.kawalcovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kawalcovid.R
import com.example.kawalcovid.dataClasses.ProvinceResponse
import kotlinx.android.synthetic.main.indonesia_most_hit_list_item_layout.view.*
import kotlinx.android.synthetic.main.item_province.view.*
import java.text.NumberFormat

class IndonesiaMostHitProvinceAdapter(private var list: ArrayList<ProvinceResponse>): RecyclerView.Adapter<IndonesiaMostHitProvinceAdapter.IndonesiaMostHitProvinceViewHolder>() {

    inner class IndonesiaMostHitProvinceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(province: ProvinceResponse){
            with(itemView){
                mostHitCityName.text = province.attributes.province
                tvIndonesiaMostHitCityInfection.text = NumberFormat.getInstance().format(province.attributes.positive).toString()
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndonesiaMostHitProvinceAdapter.IndonesiaMostHitProvinceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.indonesia_most_hit_list_item_layout, parent, false)
        return IndonesiaMostHitProvinceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IndonesiaMostHitProvinceAdapter.IndonesiaMostHitProvinceViewHolder, position: Int) {
        holder.bind(list[position])
    }

}