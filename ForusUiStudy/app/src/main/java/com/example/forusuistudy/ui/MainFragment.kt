package com.example.forusuistudy.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.forusuistudy.R
import com.example.forusuistudy.adapter.ViewPagerAdapter
import com.example.forusuistudy.databinding.FragmentMainBinding
import com.example.forusuistudy.dialog.PlanAddDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 *Created By Yelim ON 2021/10/13
 */
class MainFragment: Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    lateinit var mContext: Context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        if (container != null) {
            mContext = container.context
        }
        viewPager = binding.vpTodayList
        tabLayout = binding.tlMainTabLayout
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivMainAddPlan.setOnClickListener {
            showDialog()
        }

        val pagerAdapter = ViewPagerAdapter(requireActivity())

        pagerAdapter.addFragment(MainCalendarFragment())
        pagerAdapter.addFragment(ListFragment())
        pagerAdapter.addFragment(ListFragment())
        pagerAdapter.addFragment(ListFragment())

        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = "완료"
        }.attach()

    }

    private fun showDialog() {
        val dialog = PlanAddDialog(mContext)
        if (dialog != null) {
            dialog.callDialog()
        }

    }
}