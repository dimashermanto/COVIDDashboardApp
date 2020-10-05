package com.example.kawalcovid.api

import com.example.kawalcovid.dataClasses.*
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("indonesia")
    fun getIndonesia(): Call<ArrayList<IndonesiaResponse>>

    @GET("indonesia/provinsi")
    fun getProvince(): Call<ArrayList<ProvinceResponse>>

    @GET("positif")
    fun getGlobalPositive(): Call<GlobalPositiveResponse>

    @GET("sembuh")
    fun getGlobalHealed(): Call<GlobalHealedResponse>

    @GET("meninggal")
    fun getGlobalDeath(): Call<GlobalDeathResponse>

    @GET(".")
    fun getGlobalOverall(): Call<ArrayList<GlobalOverallResponse>>

}