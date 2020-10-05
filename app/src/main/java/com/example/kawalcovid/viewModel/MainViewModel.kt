package com.example.kawalcovid.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kawalcovid.api.RetrofitClient
import com.example.kawalcovid.dataClasses.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private var _globalPositiveTotal = MutableLiveData<String>()
    val globalPositiveTotal: LiveData<String>
        get() = _globalPositiveTotal

    private var _globalHealedTotal = MutableLiveData<String>()
    val globalHealedTotal: LiveData<String>
        get() = _globalHealedTotal

    private var _globalDeathTotal = MutableLiveData<String>()
    val globalDeathTotal: LiveData<String>
        get() = _globalDeathTotal


    private var _depokInfectedTotal = MutableLiveData<String>()
    val depokInfectedTotal: LiveData<String>
        get() = _depokInfectedTotal

    private var _jakartaInfectedTotal = MutableLiveData<String>()
    val jakartaInfectedTotal: LiveData<String>
        get() = _jakartaInfectedTotal


    private var _indonesiaPositiveTotal = MutableLiveData<String>()
    val indonesiaPositiveTotal: LiveData<String>
        get() =_indonesiaPositiveTotal

    private var _indonesiaHospitalizedTotal = MutableLiveData<String>()
    val indonesiaHospitalizedTotal: LiveData<String>
        get() =_indonesiaHospitalizedTotal

    private var _indonesiaDeathTotal = MutableLiveData<String>()
    val indonesiaDeathTotal: LiveData<String>
        get() =_indonesiaDeathTotal

    private var _indonesiaHealedTotal = MutableLiveData<String>()
    val indonesiaHealedTotal: LiveData<String>
        get() =_indonesiaHealedTotal

    private var _indonesiaMostHitProvinceList = MutableLiveData<ArrayList<ProvinceResponse>>()
    val indonesiaMostHitProvinceList: LiveData<ArrayList<ProvinceResponse>>
        get() = _indonesiaMostHitProvinceList

    private var _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private var _provinceButtonEnabledStatus = MutableLiveData<Boolean>()
    val provinceButtonEnabledStatus: LiveData<Boolean>
        get() = _provinceButtonEnabledStatus

    private var _countriesButtonEnabledStatus = MutableLiveData<Boolean>()
    val countriesButtonEnabledStatus: LiveData<Boolean>
        get() = _countriesButtonEnabledStatus




    init {
        _indonesiaPositiveTotal.value = "Loading..."
        _indonesiaHospitalizedTotal.value = "Loading..."
        _indonesiaDeathTotal.value = "Loading..."
        _indonesiaHealedTotal.value = "Loading..."

        _globalPositiveTotal.value = "Loading..."
        _globalDeathTotal.value = "Loading..."
        _globalHealedTotal.value = "Loading..."

        _jakartaInfectedTotal.value = "Loading..."
        _depokInfectedTotal.value = "Loading..."

        _indonesiaMostHitProvinceList = MutableLiveData<ArrayList<ProvinceResponse>>()

        _refreshStatus.value = false
    }


    fun showGlobalOverall(){
        showGlobalDeath()
        showGlobalPositive()
        showGlobalHealed()
    }





    private fun showGlobalPositive() {

        _globalPositiveTotal.value = "Loading..."
        _countriesButtonEnabledStatus.value = false

        RetrofitClient.instance.getGlobalPositive().enqueue(object :
            Callback<GlobalPositiveResponse> {
            override fun onFailure(call: Call<GlobalPositiveResponse>, t: Throwable) {
                _globalPositiveTotal.value = "N/A"
            }

            override fun onResponse(
                call: Call<GlobalPositiveResponse>,
                response: Response<GlobalPositiveResponse>
            ) {
                _globalPositiveTotal.value = response.body()?.value.toString()
                _countriesButtonEnabledStatus.value = true
            }

        })
    }

    private fun showGlobalHealed() {
        _globalHealedTotal.value = "Loading..."

        RetrofitClient.instance.getGlobalHealed().enqueue(object : Callback<GlobalHealedResponse> {
            override fun onFailure(call: Call<GlobalHealedResponse>, t: Throwable) {
                _globalHealedTotal.value = "N/A"
            }

            override fun onResponse(
                call: Call<GlobalHealedResponse>,
                response: Response<GlobalHealedResponse>
            ) {
                _globalHealedTotal.value = response.body()?.value.toString()
            }
        })
    }

    private fun showGlobalDeath() {

        _globalDeathTotal.value = "Loading..."

        RetrofitClient.instance.getGlobalDeath().enqueue(object : Callback<GlobalDeathResponse> {
            override fun onFailure(call: Call<GlobalDeathResponse>, t: Throwable) {
                _globalDeathTotal.value = "N/A"
            }

            override fun onResponse(
                call: Call<GlobalDeathResponse>,
                response: Response<GlobalDeathResponse>
            ) {
                _globalDeathTotal.value = response.body()?.value.toString()
            }
        })
    }


    fun showIndonesia() {
        _indonesiaPositiveTotal.value = "Loading..."
        _indonesiaHospitalizedTotal.value = "Loading..."
        _indonesiaDeathTotal.value = "Loading..."
        _indonesiaHealedTotal.value = "Loading..."

        _provinceButtonEnabledStatus.value = false

        RetrofitClient.instance.getIndonesia().enqueue(object :
            Callback<ArrayList<IndonesiaResponse>> {
            override fun onFailure(call: Call<ArrayList<IndonesiaResponse>>, t: Throwable) {
                _indonesiaPositiveTotal.value = "N/A"
                _indonesiaHospitalizedTotal.value = "N/A"
                _indonesiaHealedTotal.value = "N/A"
                _indonesiaDeathTotal.value = "N/A"

                _refreshStatus.value = false
            }

            override fun onResponse(
                call: Call<ArrayList<IndonesiaResponse>>,
                response: Response<ArrayList<IndonesiaResponse>>
            ) {
                val indonesiaResponseBody = response.body()?.get(0)
                val positiveCount = indonesiaResponseBody?.positif.toString()
                val hospitalizedCount = indonesiaResponseBody?.dirawat.toString()
                val recoveredCount = indonesiaResponseBody?.sembuh.toString()
                val deathCOunt = indonesiaResponseBody?.meninggal.toString()

                _indonesiaPositiveTotal.value = positiveCount
                _indonesiaHospitalizedTotal.value = hospitalizedCount
                _indonesiaHealedTotal.value = recoveredCount
                _indonesiaDeathTotal.value = deathCOunt

                _refreshStatus.value = false
                _provinceButtonEnabledStatus.value = true

            }

        })
    }

}