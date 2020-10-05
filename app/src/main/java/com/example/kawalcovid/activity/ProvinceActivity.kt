package com.example.kawalcovid.activity

import android.icu.text.NumberFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kawalcovid.adapter.ProvinceAdapter
import com.example.kawalcovid.R
import com.example.kawalcovid.adapter.ProvinceListOnItemClickListener
import com.example.kawalcovid.api.RetrofitClient
import com.example.kawalcovid.dataClasses.ProvinceResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.stone.vega.library.VegaLayoutManager
import kotlinx.android.synthetic.main.activity_global.*
import kotlinx.android.synthetic.main.activity_province.*
import kotlinx.android.synthetic.main.country_list_dialog_layout.view.*
import kotlinx.android.synthetic.main.country_list_dialog_layout.view.deathTotal
import kotlinx.android.synthetic.main.country_list_dialog_layout.view.infectedTotal
import kotlinx.android.synthetic.main.country_list_dialog_layout.view.recoveredTotal
import kotlinx.android.synthetic.main.province_list_dialog_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ProvinceActivity : AppCompatActivity(), ProvinceListOnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    private lateinit var result: ArrayList<ProvinceResponse>
    lateinit var adapter: ProvinceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_province)

        showProvince()

        provinceRecylerViewBaseSwipeLayout.setOnRefreshListener(this)

        findViewById<TextInputEditText>(R.id.SearchBarEditText).addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterString(s.toString())
            }
        })
    }

    fun filterString(text: String) {
        val filteredList: ArrayList<ProvinceResponse> = ArrayList()

        for (item in result) {
            if (item.attributes.province.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }

    private fun showProvince() {

        findViewById<TextInputLayout>(R.id.SearchBarInputLayout).isEnabled = false

        rvProvince.setHasFixedSize(true)
        rvProvince.visibility = View.GONE

        RetrofitClient.instance.getProvince().enqueue(object: Callback<ArrayList<ProvinceResponse>>{
            override fun onFailure(call: Call<ArrayList<ProvinceResponse>>, t: Throwable) {
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                findViewById<TextInputLayout>(R.id.SearchBarInputLayout).helperText = t.localizedMessage

                Toast.makeText(this@ProvinceActivity, t.message, Toast.LENGTH_SHORT).show()

                provinceRecylerViewBaseSwipeLayout.isRefreshing = false
            }

            override fun onResponse(
                call: Call<ArrayList<ProvinceResponse>>,
                response: Response<ArrayList<ProvinceResponse>>
            ) {
                findViewById<TextInputLayout>(R.id.SearchBarInputLayout).isEnabled = true
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                rvProvince.visibility = View.VISIBLE

                try {
                    val list = response.body()
                    adapter = list?.let {
                        ProvinceAdapter(
                            it, this@ProvinceActivity
                        )
                    }!!
                    rvProvince.setHasFixedSize(true)
                    rvProvince.onFlingListener = null
                    rvProvince.layoutManager = VegaLayoutManager()
                    rvProvince.adapter = adapter

                    provinceRecylerViewBaseSwipeLayout.isRefreshing = false

                    result = response.body()!!
                }catch (e: NullPointerException){
                    Toast.makeText(this@ProvinceActivity, "NullFuckingPointerException returned", Toast.LENGTH_SHORT).show()
                }catch (e: TypeCastException){
                    Toast.makeText(this@ProvinceActivity, "TypeCastException returned", Toast.LENGTH_SHORT).show()
                }



            }

        })
    }


    override fun onRefresh() {
        showProvince()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemClickListener(item: ProvinceResponse) {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(
            R.layout.province_list_dialog_layout, null
        )

        mDialogView.provinceId.text = item.attributes.code.toString()
        mDialogView.provinceName.text = item.attributes.province
        mDialogView.infectedTotal.text =
            NumberFormat.getInstance().format(item.attributes.positive).toString()
        mDialogView.deathTotal.text =
            NumberFormat.getInstance().format(item.attributes.death).toString()
        mDialogView.recoveredTotal.text =
            NumberFormat.getInstance().format(item.attributes.recover).toString()

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val mAlertDialog = mBuilder.show()

        mDialogView.provinceCloseButton.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }


    }
}
