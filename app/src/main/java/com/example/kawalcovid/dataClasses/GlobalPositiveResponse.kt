package com.example.kawalcovid.dataClasses

import com.google.gson.annotations.SerializedName

data class GlobalPositiveResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)
