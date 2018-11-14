package com.example.lenovo.match_schedule

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.example.lenovo.match_schedule.R.id.*
import com.example.lenovo.match_schedule.adapter.TabPageAdapter
import com.example.lenovo.match_schedule.fragment.FavoriteMatchFragment
import com.example.lenovo.match_schedule.fragment.NextMatchFragment
import com.example.lenovo.match_schedule.fragment.PastMatchFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_pager.*

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var tabPageAdapter: TabPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*viewPager = view_pager

        tabPageAdapter = TabPageAdapter(supportFragmentManager)
        viewPager.adapter = tabPageAdapter

        tabLayout = tabs
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.soccer)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.calendar)*/

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                past_match -> {
                    loadPastMatchFragment(savedInstanceState)
                }
                next_match -> {
                    loadNextMatchFragment(savedInstanceState)
                }
                favorites -> {
                    loadFavoriteMatchFragment(savedInstanceState)
                }
            }
            true
        }
        bottom_navigation.selectedItemId = past_match
    }

    private fun loadNextMatchFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, NextMatchFragment(), NextMatchFragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun loadFavoriteMatchFragment(savedInstanceState: Bundle?){
        if(savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, FavoriteMatchFragment(), FavoriteMatchFragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun loadPastMatchFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, PastMatchFragment(), PastMatchFragment::class.java.simpleName)
                    .commit()
        }
    }
}
