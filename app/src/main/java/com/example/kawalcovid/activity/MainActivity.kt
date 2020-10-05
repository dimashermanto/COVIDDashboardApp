package com.example.kawalcovid.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kawalcovid.R
import com.example.kawalcovid.adapter.IndonesiaMostHitProvinceAdapter
import com.example.kawalcovid.api.RetrofitClient
import com.example.kawalcovid.dataClasses.ProvinceResponse
import com.example.kawalcovid.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.showIndonesia()
        viewModel.showGlobalOverall()
        getIndonesianProvinceData()

        setupObserverOfDashboardData()

        rvIndonesiaMostHit.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        globaSeeAllButton.setOnClickListener {
            navigateToCountryList()
        }


        indonesiaSeeAllButton.setOnClickListener {
            navigateToProvinceList()
        }
    }

    private fun getIndonesianProvinceData(){

        tvJakartaInfection.text = "Loading"
        tvWestJavaInfection.text = "Loading"

        RetrofitClient.instance.getProvince().enqueue(object:
            Callback<ArrayList<ProvinceResponse>> {
            override fun onFailure(call: Call<ArrayList<ProvinceResponse>>, t: Throwable) {
                tvJakartaInfection.text = "N/A"
                tvWestJavaInfection.text = "N/A"
            }

            override fun onResponse(
                call: Call<ArrayList<ProvinceResponse>>,
                response: Response<ArrayList<ProvinceResponse>>
            ) {
                val jakartaAttributes = response.body()?.filter { it.attributes.province == "DKI Jakarta" }
                val jakartaPositive = jakartaAttributes?.get(0)?.attributes?.positive

                val westJavaAttributes = response.body()?.filter { it.attributes.province == "Jawa Barat" }
                val westJavaPositive = westJavaAttributes?.get(0)?.attributes?.positive

                Log.i("MainVM", "Most Hit Province : ${response.body()?.slice(0..4)}")

                rvIndonesiaMostHit.adapter = IndonesiaMostHitProvinceAdapter(response.body()?.slice(0..5) as ArrayList<ProvinceResponse>)

                tvWestJavaInfection.text = "$westJavaPositive"
                tvJakartaInfection.text = "$jakartaPositive"
            }
        })
    }


    private fun setupObserverOfDashboardData() {

        viewModel.globalPositiveTotal.observe(this, Observer { it ->
            tvGlobalPositive.text = it
        })

        viewModel.globalHealedTotal.observe(this, Observer { it ->
            tvGlobalRecover.text = it
        })

        viewModel.globalDeathTotal.observe(this, Observer { it ->
            tvGlobalDeath.text = it
        })

        tvGlobalHospitalized.text = "N/A"


        viewModel.indonesiaPositiveTotal.observe(this, Observer { it ->
            tvIndonesiaPositive.text = it
        })

        viewModel.indonesiaHospitalizedTotal.observe(this, Observer { it ->
            tvIndonesiaHospitalized.text = it
        })

        viewModel.indonesiaHealedTotal.observe(this, Observer { it ->
            tvIndonesiaRecover.text = it
        })

        viewModel.indonesiaDeathTotal.observe(this, Observer { it ->
            tvIndonesiaDeath.text = it
        })


        viewModel.provinceButtonEnabledStatus.observe(this, Observer {
            indonesiaSeeAllButton.isEnabled = it

        })

        viewModel.countriesButtonEnabledStatus.observe(this, Observer {
            globaSeeAllButton.isEnabled = it
        })

    }

    private fun navigateToCountryList(){
        Log.i("Country", "Clicked!")
        Intent(this@MainActivity, GlobalActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun navigateToProvinceList(){
        Log.i("Province", "Clicked!")

        Intent(this@MainActivity, ProvinceActivity::class.java).also {
            startActivity(it)
        }
    }


    override fun onRefresh() {
        viewModel.showIndonesia()
        viewModel.showGlobalOverall()
    }


}
