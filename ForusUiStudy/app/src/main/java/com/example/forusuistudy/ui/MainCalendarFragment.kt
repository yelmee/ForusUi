package com.example.forusuistudy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.forusuistudy.R
import com.example.forusuistudy.adapter.CalendarAdapter
import com.example.forusuistudy.databinding.ActivityCalendarBinding
import com.example.forusuistudy.ui.CalendarFragment.Companion.removeRectView

class MainCalendarFragment : Fragment() {

    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ActivityCalendarBinding>(
            inflater,
            R.layout.activity_calendar,
            container,
            false
        )

        calendarAdapter = CalendarAdapter(requireActivity())

        binding.calendar.adapter = calendarAdapter
        binding.calendar.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.calendar.setCurrentItem(CalendarAdapter.START_POSITION, false)

        binding.calendar.apply {
            adapter = calendarAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            setCurrentItem(CalendarAdapter.START_POSITION, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    removeRectView()
                }
            })
        }

        return binding.root
    }
}