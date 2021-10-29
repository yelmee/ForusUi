package com.example.forusuistudy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.forusuistudy.R
import com.example.forusuistudy.adapter.CalendarAdapter
import com.example.forusuistudy.adapter.CalendarAdapter.Companion.start
import com.example.forusuistudy.custom.RectView
import com.example.forusuistudy.databinding.ActivityCalendarBinding
import com.example.forusuistudy.ui.CalendarFragment.Companion.removeRectView
import com.example.forusuistudy.utils.CalendarUtils.Companion.changeLongToString
import com.example.forusuistudy.utils.CalendarUtils.Companion.changeLongToString2
import org.joda.time.DateTime

class CalendarFrameFragment : Fragment() {

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
                    Log.d("jyl", "${this.javaClass.name}: onPageSelected")
                     val date = DateTime(start).plusMonths(position - CalendarAdapter.START_POSITION).millis

                    onMonthListener?.invoke(changeLongToString2(date))
                    removeRectView()
                }
            })
        }

        return binding.root
    }

    companion object{
        var onMonthListener: ((String) -> Unit)? = null

    }
}