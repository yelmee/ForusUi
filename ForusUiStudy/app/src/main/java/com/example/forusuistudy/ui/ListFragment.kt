package com.example.forusuistudy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.forusuistudy.R
import com.example.forusuistudy.adapter.ListRecyclerViewAdapter
import com.example.forusuistudy.data.Plan
import com.example.forusuistudy.databinding.FragmentListBinding
import com.example.forusuistudy.utils.CalendarUtils.Companion.addTime

/**
 *Created By Yelim ON 2021/10/13
 */
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private var listAdapter = ListRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list,
            container,
            false
        )
        binding.rcList.adapter = listAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = Plan(1, "CSS 만들기", addTime(3), addTime(10), "http://naver.com", 13, 100)
        val list = mutableListOf<Plan>()
        list.add(item)
        list.add(item)
        list.add(item)
        listAdapter.submitList(list)
        listAdapter.notifyDataSetChanged()
    }
}