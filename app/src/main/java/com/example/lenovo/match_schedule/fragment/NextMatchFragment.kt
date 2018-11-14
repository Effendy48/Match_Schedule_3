package com.example.lenovo.match_schedule.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.service.autofill.FieldClassification
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import com.example.lenovo.match_schedule.DetailMatch

import com.example.lenovo.match_schedule.R
import com.example.lenovo.match_schedule.adapter.MatchAdapter
import com.example.lenovo.match_schedule.api.ApiInterface
import com.example.lenovo.match_schedule.api.ApiRepository
import com.example.lenovo.match_schedule.model.Match
import com.example.lenovo.match_schedule.model.MatchResponse
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class NextMatchFragment : Fragment(), AnkoLogger {
    private val nextMatch: MutableList<Match> = mutableListOf()
    private lateinit var MAdapter: MatchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = with(container) {
        MAdapter = MatchAdapter(nextMatch) {
            startActivity<DetailMatch>(DetailMatch.POSITION_EXTRA to it)
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
                        adapter = MAdapter
                    }
                }
            }
        }.view
    }

    private fun loadData(){
        val api : ApiInterface = ApiRepository.getRetrofit().create(ApiInterface::class.java)
        val call = api.getNextMatch()

        call.enqueue(object : retrofit2.Callback<MatchResponse> {

            override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>){
                   if (response.isSuccessful()){
                       val match : List<Match> = response.body()?.matchs!!
                       var msg = ""
                       for (item : Match? in match.iterator()){
                           msg = msg + item?.strEvent + "\n"
                           nextMatch.clear()
                           nextMatch.addAll(match)
                           MAdapter.notifyDataSetChanged()
                       }
                       Log.e("Api", match.toString()+ "\n")
                       Log.e("Count", msg)
                   }
            }
            override fun onFailure(call : Call<MatchResponse>, t:Throwable){
                toast("Error").show()
            }
        })
    }











}
