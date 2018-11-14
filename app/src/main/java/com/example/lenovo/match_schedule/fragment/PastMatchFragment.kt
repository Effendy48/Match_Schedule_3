package com.example.lenovo.match_schedule.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.service.autofill.FieldClassification
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.lenovo.match_schedule.DetailMatch
import com.example.lenovo.match_schedule.R
import com.example.lenovo.match_schedule.adapter.MatchAdapter
import com.example.lenovo.match_schedule.api.ApiInterface
import com.example.lenovo.match_schedule.api.ApiRepository
import com.example.lenovo.match_schedule.model.Match
import com.example.lenovo.match_schedule.model.MatchResponse
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.*
import retrofit2.Callback

class PastMatchFragment : Fragment(),AnkoLogger {

    private val matchs : MutableList<Match> = mutableListOf()
    private lateinit var mAdapter : MatchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = with(container) {
        mAdapter = MatchAdapter(matchs){
            startActivity<DetailMatch>(DetailMatch.POSITION_EXTRA to it)
            longToast(it.strEvent.toString())
        }
        loadData()
        return UI {
           linearLayout {
                lparams(matchParent, matchParent)
                orientation = LinearLayout.VERTICAL

                swipeRefreshLayout {
                    onRefresh {
                        Handler().postDelayed({
                            setColorSchemeColors(
                                    resources.getColor(R.color.colorPrimary),
                                    resources.getColor(R.color.colorPrimaryDark),
                                    resources.getColor(R.color.colorAccent)
                            )
                            loadData()
                            isRefreshing = false
                        }, 3000)
                    }
                    recyclerView {
                        lparams(matchParent, matchParent)
                        layoutManager = LinearLayoutManager(ctx)
                        adapter = mAdapter
                    }
                }
            }
        }.view
    }
    private fun loadData(){
        val api : ApiInterface = ApiRepository.getRetrofit().create(ApiInterface::class.java)
        val call = api.getPastMatch()
        call.enqueue(object : Callback<MatchResponse> {
            override fun onFailure(call: retrofit2.Call<MatchResponse>, t: Throwable) {
                toast("Error").show()
            }

            override fun onResponse(call: retrofit2.Call<MatchResponse>, response: retrofit2.Response<MatchResponse>) {

                if (response.isSuccessful()) {

                    val match: List<Match> = response.body()?.matchs!!
                    var msg = ""
                    for (item: Match? in match.iterator()) {
                        msg = msg + item?.strEvent + "\n"
                        matchs.clear()
                        matchs.addAll(match)
                        mAdapter.notifyDataSetChanged()
                    }
                    Log.e("API", match.toString() + "\n")
                    Log.e("Count,", msg)
                }
            }
        })
    }
}
