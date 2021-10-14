package com.example.forusuistudy.adapter

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.forusuistudy.ui.CalendarFragment
import org.joda.time.DateTime

class CalendarAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    /* 달의 첫 번째 Day timeInMillis */
    private var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): CalendarFragment {
        Log.d("jyl","createFragment: " + position)
        val millis = getItemId(position)

        return CalendarFragment.newInstance(millis)
    }

    override fun getItemId(position: Int): Long {
        Log.d("jyl","dataTime: " + DateTime(start).plusMonths(position - START_POSITION).toString("YYYY-MM-dd"))
        Log.d("jyl","position: " + position)
        Log.d("jyl","START_POSITION: " + START_POSITION)
        Log.d("jyl","getItemId: " + DateTime(start).plusMonths(position - START_POSITION).millis)
        Log.d("jyl","DateTime(start): " + DateTime(start).toString("YYYY-MM-dd"))
        return DateTime(start).plusMonths(position - START_POSITION).millis
    }

    override fun containsItem(itemId: Long): Boolean {
        val date = DateTime(itemId)
        Log.d("jyl","itemId: " + itemId)
        Log.d("jyl","date.: " + date.toString("YYYY-MM"))
        Log.d("jyl","date.millisOfDay: " + date.millisOfDay)

        return date.dayOfMonth == 1 && date.millisOfDay == 0
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}