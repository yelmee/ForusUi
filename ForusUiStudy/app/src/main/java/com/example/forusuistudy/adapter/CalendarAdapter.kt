package com.example.forusuistudy.adapter

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.forusuistudy.ui.CalendarFragment
import com.example.forusuistudy.utils.CalendarUtils.Companion.changeLongToString
import org.joda.time.DateTime

class CalendarAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    /* 달의 첫 번째 Day timeInMillis */

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): CalendarFragment {
        val millis = getItemId(position)

        Log.d("jyl", "${this.javaClass.name}: createFragment ${changeLongToString(millis)}")
        return CalendarFragment.newInstance(millis)
    }

    override fun getItemId(position: Int): Long {
        Log.d("jyl", "${this.javaClass.name}: getItemId ${changeLongToString(DateTime(start).plusMonths(position - START_POSITION).millis)}")
        return DateTime(start).plusMonths(position - START_POSITION).millis
    }

    override fun containsItem(itemId: Long): Boolean {
        val date = DateTime(itemId)
        Log.d("jyl", "${this.javaClass.name}: containsItem ${changeLongToString(itemId)}")
        return date.dayOfMonth == 1 && date.millisOfDay == 0
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
        var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis
    }
}