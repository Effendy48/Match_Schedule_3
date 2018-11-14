package com.example.lenovo.match_schedule.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.lenovo.match_schedule.fragment.NextMatchFragment
import com.example.lenovo.match_schedule.fragment.PastMatchFragment

class TabPageAdapter(fm : FragmentManager?) : FragmentPagerAdapter(fm) {
    val PAGE_COUNT = 2
    val titleTab = arrayOf("Past Match", "Next Match")
    override fun getItem(position: Int): Fragment? {
        return when(position){
            0 ->{
                PastMatchFragment()
            }
            1 ->{
                NextMatchFragment()
            }
            else -> {
                return null
            }
        }
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return titleTab[position]
    }
}