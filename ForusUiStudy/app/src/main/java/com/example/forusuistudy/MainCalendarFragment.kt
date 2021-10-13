package com.leveloper.infinitecalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.forusuistudy.R
import com.example.forusuistudy.databinding.ActivityCalendarBinding
import java.util.*
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

        return binding.root
    }
}