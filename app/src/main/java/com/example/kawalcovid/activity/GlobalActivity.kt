package com.example.kawalcovid.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.NumberFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kawalcovid.R
import com.example.kawalcovid.adapter.CountryAdapter
import com.example.kawalcovid.adapter.CountryListOnItemClickListener
import com.example.kawalcovid.api.RetrofitClient
import com.example.kawalcovid.dataClasses.GlobalOverallResponse
import com.google.android.material.textfield.TextInputEditText
import com.stone.vega.library.VegaLayoutManager
import kotlinx.android.synthetic.main.activity_global.*
import kotlinx.android.synthetic.main.country_list_dialog_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class GlobalActivity : AppCompatActivity(), CountryListOnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    //    private lateinit var viewModel: GlobalActivityViewModel
    lateinit var resultsForSearchUtility: ArrayList<GlobalOverallResponse>
    lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global)

        showCountries()

        globalRecylerViewBaseSwipeLayout.setOnRefreshListener(this)

        findViewById<TextInputEditText>(R.id.countrySearchBarEditText).addTextChangedListener(object :
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


    private fun showCountries() {

        rvCountry.visibility = View.GONE
        countrySearchBarInputLayout.isEnabled = false

        RetrofitClient.instance.getGlobalOverall()
            .enqueue(object : Callback<ArrayList<GlobalOverallResponse>> {
                override fun onFailure(call: Call<ArrayList<GlobalOverallResponse>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<GlobalOverallResponse>>,
                    response: Response<ArrayList<GlobalOverallResponse>>
                ) {

                    rvCountry.visibility = View.VISIBLE
                    countrySearchBarInputLayout.isEnabled = true

                    val countryList = response.body()

                    try {
                        adapter = countryList?.let {
                            CountryAdapter(
                                it, this@GlobalActivity
                            )
                        }!!

                        rvCountry.setHasFixedSize(true)
                        rvCountry.onFlingListener = null
                        rvCountry.layoutManager = VegaLayoutManager()
                        rvCountry.adapter = adapter

                        globalProgressBar.visibility = View.GONE
                        globalRecylerViewBaseSwipeLayout.isRefreshing = false

                        resultsForSearchUtility = response.body()!!

                    } catch (e: NullPointerException) {
                        Toast.makeText(
                            this@GlobalActivity,
                            "NullFuckingPointerException returned",
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: TypeCastException) {
                        Toast.makeText(
                            this@GlobalActivity,
                            "TypeCastException returned",
                            Toast.LENGTH_SHORT
                        ).show()

                    }


                }
            })
    }


    fun filterString(text: String) {
        val filteredList: ArrayList<GlobalOverallResponse> = ArrayList()

        for (item in resultsForSearchUtility) {
            if (item.attributes.country.toLowerCase(Locale.ROOT)
                    .contains(text.toLowerCase(Locale.ROOT))
            ) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }

    override fun onRefresh() {
        showCountries()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemClickListener(item: GlobalOverallResponse) {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(
            R.layout.country_list_dialog_layout, null
        )

        mDialogView.countryId.text = item.attributes.OBJECTID.toString()
        mDialogView.countryName.text = item.attributes.country
        mDialogView.infectedTotal.text =
            NumberFormat.getInstance().format(item.attributes.positive).toString()
        mDialogView.deathTotal.text =
            NumberFormat.getInstance().format(item.attributes.death).toString()
        mDialogView.recoveredTotal.text =
            NumberFormat.getInstance().format(item.attributes.recovered).toString()
        mDialogView.currentInfected.text =
            NumberFormat.getInstance().format(item.attributes.active).toString()

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)



        //show dialog
        val mAlertDialog = mBuilder.show()

        mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mDialogView.countryCloseButton.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }


}
