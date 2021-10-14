package com.example.forusuistudy.utils

import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDateTime
import org.joda.time.Weeks
import org.joda.time.format.DateTimeFormat

class CalendarUtils {

    companion object {

        const val WEEKS_PER_MONTH = 6
        private val fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")

        /**
         * 선택된 날짜에 해당하는 월간 달력을 반환한다.
         */
        fun getMonthList(dateTime: DateTime): Pair<List<DateTime>, Int> {
            val list = mutableListOf<DateTime>()

            val date = dateTime.withDayOfMonth(1)
            val prev = getPrevOffSet(date)

            val startValue = date.minusDays(prev)

            val totalDay = DateTimeConstants.DAYS_PER_WEEK * WEEKS_PER_MONTH

            for (i in 0 until totalDay) {
                list.add(DateTime(startValue.plusDays(i)))
            }


            return Pair(list, prev)
        }

        /**
         * 해당 calendar 의 이전 달의 일 갯수를 반환한다.
         */
        fun getPrevOffSet(dateTime: DateTime): Int {
            var prevMonthTailOffset = dateTime.dayOfWeek

            if (prevMonthTailOffset >= 7) prevMonthTailOffset %= 7

            return prevMonthTailOffset
        }

        /**
         * 같은 달인지 체크
         */
        fun isSameMonth(first: DateTime, second: DateTime): Boolean =
            first.year == second.year && first.monthOfYear == second.monthOfYear

        /**
         * 해당 요일의 색깔을 반환한다.
         * 일요일 -> 빨간색
         * 토요일 -> 파란색
         * 나머지 -> 검정색
         */
        @ColorInt
        fun getDateColor(@IntRange(from=1, to=7) dayOfWeek: Int): Int {
            return when (dayOfWeek) {
                /* 토요일은 파란색 */
                DateTimeConstants.SATURDAY -> Color.parseColor("#2962FF")
                /* 일요일 빨간색 */
                DateTimeConstants.SUNDAY -> Color.parseColor("#D32F2F")
                /* 그 외 검정색 */
                else -> Color.parseColor("#000000")
            }
        }

        fun getWeekOfMonth(date: DateTime): Int {
            val totalDay = DateTimeConstants.DAYS_PER_WEEK * WEEKS_PER_MONTH
            Log.d("jyl", "minusDays: "+date.withDayOfMonth(1).minusDays(getPrevOffSet(date.withDayOfMonth(1))))
            Log.d("jyl", "plusDays: "+(date))
            return Weeks.weeksBetween(
                date.withDayOfMonth(1).minusDays(getPrevOffSet(date.withDayOfMonth(1))),
                date).weeks + 1
        }

        fun addTime(day: Int): String? {
            val date = DateTime().withDayOfMonth(day).withTimeAtStartOfDay()
            return date.toString(fmt)
        }
    }
}