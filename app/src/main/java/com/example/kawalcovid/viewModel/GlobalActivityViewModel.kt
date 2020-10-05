package com.example.kawalcovid.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kawalcovid.adapter.CountryAdapter
import com.example.kawalcovid.adapter.CountryListOnItemClickListener
import com.example.kawalcovid.api.RetrofitClient
import com.example.kawalcovid.dataClasses.GlobalOverallResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class GlobalActivityViewModel: ViewModel(), CountryListOnItemClickListener {

    private var _result = MutableLiveData<ArrayList<GlobalOverallResponse>>()
    val result: LiveData<ArrayList<GlobalOverallResponse>>
        get() = _result

    private var _loadStatus = MutableLiveData<Boolean>()
    val loadStatus : LiveData<Boolean>
        get() = _loadStatus

    lateinit var adapter: CountryAdapter

    init {
        _loadStatus.value = false
    }

    fun showCountries() {
        RetrofitClient.instance.getGlobalOverall()
            .enqueue(object : Callback<ArrayList<GlobalOverallResponse>> {
                override fun onFailure(call: Call<ArrayList<GlobalOverallResponse>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<GlobalOverallResponse>>,
                    response: Response<ArrayList<GlobalOverallResponse>>
                ) {
                    _loadStatus.value = true

                    Log.i("Country", response.body().toString())

                    val list = response.body()
                    adapter = list?.let {
                        CountryAdapter(
                            it, this@GlobalActivityViewModel
                        )
                    }!!

                    _result.value = response.body()!!

                }
            })
    }


    fun filterString(text: String) {
        val filteredList: ArrayList<GlobalOverallResponse> = ArrayList()

        for (item in _result.value!!) {
            if (item.attributes.country.toLowerCase(Locale.ROOT)
                    .contains(text.toLowerCase(Locale.ROOT))
            ) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }

    override fun onItemClickListener(item: GlobalOverallResponse) {
        //Do Nothing
    }

}