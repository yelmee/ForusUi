package com.leveloper.infinitecalendar

import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.forusuistudy.R
import com.example.forusuistudy.databinding.FragmentCalendarBinding
import com.leveloper.infinitecalendar.utils.CalendarUtils.Companion.getMonthList
import com.leveloper.infinitecalendar.utils.CalendarUtils.Companion.getWeekOfMonth
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

class CalendarFragment : Fragment() {

    private var millis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millis = it.getLong(MILLIS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCalendarBinding>(
            inflater,
            R.layout.fragment_calendar,
            container,
            false
        )

        binding.millis.text = DateTime(millis).toString("yyyy-MM")
        binding.calendarView.initCalendar(DateTime(millis), getMonthList(DateTime(millis)).first)

        prevCount = getMonthList(DateTime(millis)).second

        Log.d("jyl","week:"+getWeekOfMonth(DateTime().withDayOfMonth(14)))

//        view.rectangleView.initRect()
        return binding.root
    }

    companion object {

        private const val MILLIS = "MILLIS"
        var prevCount = 0

        fun newInstance(millis: Long) = CalendarFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
}