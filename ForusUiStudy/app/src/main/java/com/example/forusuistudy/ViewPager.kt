package com.example.forusuistudy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *Created By Yelim ON 2021/10/13
 */
class ViewPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    val fragments: ArrayList<Fragment> = ArrayList()
    companion object{
        private const val NUM_PAGES = 1
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
           return fragments[position]
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size-1)
    }
}