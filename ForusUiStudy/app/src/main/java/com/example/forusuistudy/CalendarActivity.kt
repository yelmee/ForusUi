package com.leveloper.infinitecalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*
import java.util.zip.Inflater

class CalendarActivity : AppCompatActivity() {

    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarAdapter = CalendarAdapter(this)

        calendar.adapter = calendarAdapter
        calendar.setCurrentItem(CalendarAdapter.START_POSITION, false)

    }
}