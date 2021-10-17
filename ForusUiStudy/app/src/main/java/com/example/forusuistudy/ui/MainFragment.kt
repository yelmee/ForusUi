package com.example.forusuistudy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.forusuistudy.R
import com.example.forusuistudy.adapter.ViewPagerAdapter
import com.example.forusuistudy.databinding.FragmentMainBinding
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
    @SuppressLint("RestrictedApi")
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
        val toolbar = binding.toolbar

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDefaultDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_add_black_36)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        return binding.root

    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivMainAddPlan.setOnClickListener {
                val navDirections = MainFragmentDirections.actionMainFragmentToPlanAddDialog2()
                it.findNavController().navigate(navDirections)
        }

        val pagerAdapter = ViewPagerAdapter(requireActivity())

        pagerAdapter.addFragment(CalendarFrameFragment())
        pagerAdapter.addFragment(ListFragment())
        pagerAdapter.addFragment(ListFragment())
        pagerAdapter.addFragment(ListFragment())

        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = "완료"
        }.attach()

//        val navController = Navigation.findNavController(activity?.parent!!, R.id.mainFragment)
//        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()


//        NavigationUI.setupWithNavController(
//            toolbar, navController, appBarConfiguration
//        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val menuInflater = activity?.menuInflater
        menuInflater?.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onResume() {
        super.onResume()
        Log.d("jyl","onResume MainFragment")
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(context, "검색하기", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.home -> {
                Toast.makeText(context, "뒤로가기", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}