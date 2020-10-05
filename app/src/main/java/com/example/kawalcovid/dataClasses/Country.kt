package com.example.kawalcovid.dataClasses

import com.google.gson.annotations.SerializedName

data class Country (
    @SerializedName("OBJECTID")
    val OBJECTID: Int,

    @SerializedName("Country_Region")
    val country: String,

    @SerializedName("Confirmed")
    val positive: Int,

    @SerializedName("Deaths")
    val death: Int,

    @SerializedName("Recovered")
    val recovered: Int,

    @SerializedName("Active")
    val active: Int
)