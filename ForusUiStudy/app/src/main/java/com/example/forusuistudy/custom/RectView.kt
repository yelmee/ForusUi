package com.example.forusuistudy.custom

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.example.forusuistudy.ui.CalendarFragment
import com.example.forusuistudy.R
import com.example.forusuistudy.data.Plan
import com.example.forusuistudy.data.PlanSet
import com.example.forusuistudy.utils.CalendarUtils
import com.example.forusuistudy.utils.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.example.forusuistudy.utils.CalendarUtils.Companion.addTime
import com.example.forusuistudy.utils.CalendarUtils.Companion.getPrevOffSet
import com.example.forusuistudy.utils.CalendarUtils.Companion.getWeekOfMonth
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants.DAYS_PER_WEEK
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import kotlin.collections.ArrayList
import kotlin.math.max


class RectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.rectViewStyle,
    @StyleRes defStyleRes: Int = R.style.Rect_RectViewStyle
) : FrameLayout(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    //    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    private val binding: FragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, this, false)
    private val paint = Paint()
    private val li = ArrayList<PlanSet>()

    private var list = ArrayList<ArrayList<PlanSet>>()
    private var originList = ArrayList<Plan>()
    private val fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
    private val fmtOut = DateTimeFormat.forPattern("yyyy-MM-dd")
    private var _height: Float = 0f
    private var iHeight = 0F
    private var iWidth = 0F
    private  var planSet = PlanSet(-1, "", null, null)

    init {
//        setViewModel(data)
        context.withStyledAttributes(attrs, R.styleable.RectView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.RectView_rectHeight, 0f)
        }

        li.add(PlanSet(-1, "null","null","null"))
        for (i in 0..5) {
            list.add(li)
        }
        var data = Plan(1, "알고리즘 공부", addTime(12), addTime(14), "www.naver.com", 12, 80)
        originList.add(data)
        data = Plan(1, "안드로이드 개념 공부", addTime(11), addTime(20), "www.naver.com", 12, 80)
        originList.add(data)

        val allDay = WEEKS_PER_MONTH * DAYS_PER_WEEK
        val firstDayOfMonth = DateTime().withDayOfMonth(1)
        val prevOffSet = getPrevOffSet(firstDayOfMonth)
        val firstDayOfCalendar = firstDayOfMonth.minusDays(prevOffSet)
        val sathurDayList = arrayListOf<String>() // 주의 마지막 요일인 토요일에 해당되는 날짜의 리스트
        val sathurDayNum = "6"

        /**
         * 선택된 날짜의 월에 해당하는 토요일 일자를 리스트로 담기
         */
        for (i in 0 until allDay) {
            val curDateDayOfWeek = firstDayOfCalendar.plusDays(i).dayOfWeek().asString
            if (curDateDayOfWeek.equals(sathurDayNum)) {
                sathurDayList.add(firstDayOfCalendar.plusDays(i).toString(fmtOut))
            }
        }

        /**
         * 일정 리스트를 주간 일정 리스트로 재편집
         */
        for (i in 0 until originList.size) {
            val period = Days.daysBetween(
                fmt.parseDateTime(originList[i].termFrom).toLocalDate(),
                fmt.parseDateTime(originList[i].termTo).toLocalDate()
            ).days + 1

            var firstDay = fmt.parseDateTime(originList[i].termFrom)
            for (j in 0 until period) {
                val curDate =
                    fmt.parseDateTime(originList[i].termFrom).plusDays(j)
                Log.d(TAG, "curDate: "+ curDate.toString(fmtOut))
                Log.d(TAG, "sathurDayList: ${sathurDayList[i]}")

                for (d in 0 until sathurDayList.size) {
                    if (curDate.toString(fmtOut).equals(sathurDayList[d])) {
                        planSet = PlanSet(
                            originList[i].id,
                            originList[i].title,
                            firstDay.toString(fmtOut),
                            curDate.toString(fmtOut)
                        )
                        firstDay = curDate.plusDays(1)
                    }
                }


                if (j == period - 1) {
                     planSet = PlanSet(
                        originList[i].id,
                        originList[i].title,
                        firstDay.toString(fmtOut),
                        curDate.toString(fmtOut)
                    )
                }
                val testList = ArrayList<PlanSet>()
                if (planSet.id != -1) {

                    val index = getWeekOfMonth(fmtOut.parseDateTime(planSet.termFrom)) - 1

                    for (l in 0 until list[index].size) {
                        testList.add(list[index][l])
                    }

                    testList.add(planSet)
                    list[index] = testList
                    planSet = PlanSet(-1,"",null,null)
                }
            }

            // arraylist 로그찍기
            for (i in 0 until list.size) {
                for (j in 0 until list[i].size) {
                    Log.d(TAG, "list: $i $j "+ list[i][j]+"\n")
                }
            }
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = paddingTop + paddingBottom + max(suggestedMinimumHeight, (_height * WEEKS_PER_MONTH).toInt())
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), h)
    }
//    private fun setViewModel(data: Plan) {
//        binding.planData = data
//    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
         iWidth = (width / DAYS_PER_WEEK).toFloat()
         iHeight = (height / WEEKS_PER_MONTH).toFloat()
        Log.d("jyl", "onLayout: rect $iHeight")

        initRect()
        var currentWeekIndex = 1    // array의 인덱
        val prevCount = CalendarFragment.prevCount
        children.forEach { view ->

            val topMargin = - 5
            if (currentWeekIndex >= list.size) {
                return
            }
            val row = list[currentWeekIndex - 1].size

            val rectHeight = 240

            view.layout(
                0,
                ((currentWeekIndex - 2) * iHeight.toInt()) + topMargin  ,
                width,
                ((currentWeekIndex - 2) * iHeight.toInt()) + topMargin + (row * rectHeight)
            )
            currentWeekIndex++
        }
    }

    private fun initRect() {
        var index = 0
        Log.d(TAG, "initRect: ")
        list.forEach { _ ->
            var h = iHeight
            var w = iWidth
            addView(
                RectChildView(
                    context = context,
                    list = list[index],
                    iHeight = h,
                    iWidth = w
                )
            )
            index++
        }
    }
}