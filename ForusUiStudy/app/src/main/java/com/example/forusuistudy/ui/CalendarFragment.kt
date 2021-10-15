package com.example.forusuistudy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.forusuistudy.R
import com.example.forusuistudy.databinding.FragmentCalendarBinding
import com.example.forusuistudy.utils.CalendarUtils.Companion.getMonthList
import com.example.forusuistudy.utils.CalendarUtils.Companion.getWeekOfMonth
import org.joda.time.DateTime

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
         binding = DataBindingUtil.inflate<FragmentCalendarBinding>(
            inflater,
            R.layout.fragment_calendar,
            container,
            false
        )

        Log.d("jyl", "onCreateView: start")
        binding.calendarMonthNum.text = DateTime(millis).toString("yyyy.MM")
        binding.calendarView.initCalendar(DateTime(millis), getMonthList(DateTime(millis)).first)
        binding.dayOfMonthView.initCalendar()
        prevCount = getMonthList(DateTime(millis)).second

        Log.d("jyl","week:"+getWeekOfMonth(DateTime().withDayOfMonth(14)))

//        view.rectangleView.initRect()
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("jyl", "onViewCreated: start")
    }

    companion object {
        private lateinit var binding: FragmentCalendarBinding

        fun removeRectView() {
            binding.rectangleView.removeAllViewsInLayout()

        }

        private const val MILLIS = "MILLIS"
        var prevCount = 0

        fun newInstance(millis: Long) = CalendarFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
}