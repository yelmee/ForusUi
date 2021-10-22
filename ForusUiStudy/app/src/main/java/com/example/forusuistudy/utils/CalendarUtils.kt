package com.example.forusuistudy.utils

import android.content.ContentValues
import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.example.forusuistudy.data.Plan
import com.example.forusuistudy.data.PlanSet
import com.example.forusuistudy.data.PlanWithRow
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDateTime
import org.joda.time.Weeks
import org.joda.time.format.DateTimeFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarUtils {

    companion object {

        const val WEEKS_PER_MONTH = 6
        private val fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
        private val fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd")
        private val fmt3 = DateTimeFormat.forPattern("yyyy.MM.dd")

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
            return Weeks.weeksBetween(
                date.withDayOfMonth(1).minusDays(getPrevOffSet(date.withDayOfMonth(1))),
                date).weeks + 1
        }

        fun addTime(day: Int): String? {
            val date = DateTime().withDayOfMonth(day).withTimeAtStartOfDay()
            return date.toString(fmt)
        }

        fun isOverlappingDate(startDate1: Date, endDate1: Date, startDate2: Date, endDate2: Date): Boolean {
            var isOverlap1 = false
            var isOverlap2 = false
            if (startDate1.before(endDate2)) {
                isOverlap1 = true
            }
            if (startDate2.before(endDate1)) {
                isOverlap2 = true
            }
            return isOverlap1 && isOverlap2
        }

        fun changeStringToDate(stringDate: String): Date {
           return fmt2.parseDateTime(stringDate).toDate()
        }

        fun changeLongToString(longDate: Long): String{
            return DateTime(longDate).toString(fmt3)
        }

        fun changeStringFormat(stringDate: String): String {
            return fmt.parseDateTime(stringDate).toString(fmt2)
        }

        fun getLogOfArrayOfArray(list: ArrayList<ArrayList<PlanWithRow>>) {
            /**
             * arraylist 로그찍기
             */
            for (i in 0 until list.size) {
                for (j in 0 until list[i].size) {
                    Log.d(ContentValues.TAG, "list: $i $j "+ list[i][j]+"\n")
                }
            }
        }

        fun getLogOfArray(list: ArrayList<PlanWithRow>) {
            /**
             * arraylist 로그찍기
             */
                for (i in 0 until list.size) {
                    Log.d(ContentValues.TAG, "getLogOfArray: $i "+ list[i]+"\n")
            }
        }

         fun divideStartToEnd(period: String): Pair<String, String> {

            val start = fmt3.parseDateTime(period.substring(0,10)).toString(fmt)
            val end = fmt3.parseDateTime(period.substring(11,21)).toString(fmt)
            val pair = Pair(start, end)
            return pair
        }
    }
}