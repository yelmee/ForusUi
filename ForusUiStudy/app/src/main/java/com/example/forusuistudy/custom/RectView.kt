package com.example.forusuistudy.custom

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
import com.example.forusuistudy.R
import com.example.forusuistudy.data.Plan
import com.example.forusuistudy.data.PlanSet
import com.example.forusuistudy.dialog.PlanAddDialog
import com.example.forusuistudy.ui.CalendarFrameFragment
import com.example.forusuistudy.utils.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.example.forusuistudy.utils.CalendarUtils.Companion.addTime
import com.example.forusuistudy.utils.CalendarUtils.Companion.filterMonthlyPlan
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
    var index = 3

    //    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //    private val binding: FragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, this, false)
    private val paint = Paint()
    private var emptyList = ArrayList<PlanSet>()
    private val emptyPlan = PlanSet(-1, "null", "null", "null")
    private var weeklyPlanList = ArrayList<ArrayList<PlanSet>>()
    private var planSet = PlanSet(-1, "", null, null)
    private var weekLastDayList = arrayListOf<String>() // 주의 마지막 요일인 토요일에 해당되는 날짜의 리스트

    private val fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
    private val fmtOut = DateTimeFormat.forPattern("yyyy-MM-dd")
    private val fmt2 = DateTimeFormat.forPattern("yyyy.MM.dd")
    private val fmt4 = DateTimeFormat.forPattern("MM/yyyy")

    private var _height: Float = 0f
    private var iHeight = 0F
    private var iWidth = 0F

    private val allDay = WEEKS_PER_MONTH * DAYS_PER_WEEK
    private var firstDayOfMonth = DateTime().withDayOfMonth(1)
    private var prevOffSet = getPrevOffSet(firstDayOfMonth)
    private var firstDayOfCalendar = firstDayOfMonth.minusDays(prevOffSet)
    private val lastDayOfWeekIndex = "6"
    private var curMonth = firstDayOfMonth.toString(fmt4)

    companion object {
        var monthlyPlanList = listOf<Plan>()
        var yearlyPlanList = ArrayList<Plan>()
        var onDialogDismissListener: ((Plan) -> Unit)? = null
        var onCalendarSelectedListener: ((String) -> Unit)? = null
    }

    init {
        setWillNotDraw(false)
        context.withStyledAttributes(attrs, R.styleable.RectView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.RectView_rectHeight, 0f)
        }
        emptyList.add(emptyPlan)
        initYearlyPlanList()
        initWeeklyPlanList()
        editMonthlyPlanToWeekPlan()

        PlanAddDialog.onDismissListener = onDialogDismissListener
        onDialogDismissListener = { plan ->
            Log.d("jyl", "${this.javaClass.name}: onDismissListener")

            yearlyPlanList.add(plan)
            monthlyPlanList = filterMonthlyPlan(curMonth, yearlyPlanList)

            initWeeklyPlanList()
            editMonthlyPlanToWeekPlan()

            removeAllViews()
            removeAllViews()
        }

        CalendarFrameFragment.onMonthListener = onCalendarSelectedListener
        onCalendarSelectedListener = { month ->
            this.curMonth = month
            Log.d("jyl", "${this.javaClass.name}: onMonthListener")
            Log.d("jyl", "${this.javaClass.name}: month $month")

            initWeeklyPlanList()
            firstDayOfMonth = fmt4.parseDateTime(month).withDayOfMonth(1)
            monthlyPlanList = filterMonthlyPlan(month, yearlyPlanList)

            setWeekLastDaysOfMonth()
            editMonthlyPlanToWeekPlan()
            removeAllViews()
        }
    }

    private fun initYearlyPlanList() {
        yearlyPlanList = createPlanList()
        var data = Plan(1, "일정1", addTime(5, 0), addTime(7, 0), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "일정2", addTime(22, 1), addTime(23, 1), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "일정3", addTime(22, 1), addTime(29, 1), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "일정4", addTime(18, 0), addTime(21, 0), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "일정5", addTime(17, 0), addTime(18, 0), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)
    }

    private fun createPlanList(): ArrayList<Plan>{
        return ArrayList()
    }

    private fun initWeeklyPlanList() {
        weeklyPlanList = ArrayList<ArrayList<PlanSet>>()
        for (i in 0..5) {
            weeklyPlanList.add(emptyList)
        }
    }

    private fun setWeekLastDaysOfMonth() {

        prevOffSet = getPrevOffSet(firstDayOfMonth)
        firstDayOfCalendar = firstDayOfMonth.minusDays(prevOffSet)
        /**
         * 선택된 날짜의 월에 해당하는 토요일 일자를 리스트로 담기
         */
        weekLastDayList = arrayListOf<String>()
        for (i in 0 until allDay) {
             val curDateDayOfWeek = firstDayOfCalendar.plusDays(i).dayOfWeek().asString
            if (curDateDayOfWeek.equals(lastDayOfWeekIndex)) {
                weekLastDayList.add(firstDayOfCalendar.plusDays(i).toString(fmtOut))
            }
        }
    }

    private fun editMonthlyPlanToWeekPlan() {

        /**
         * 일정 리스트를 주간 일정 리스트로 재편집
         */
        for (i in monthlyPlanList.indices) {
            val planPeriod = Days.daysBetween(
                fmt.parseDateTime(monthlyPlanList[i].startDate).toLocalDate(),
                fmt.parseDateTime(monthlyPlanList[i].endDate).toLocalDate()
            ).days + 1

            var startDateByWeek = fmt.parseDateTime(monthlyPlanList[i].startDate)
            loop@ for (j in 0 until planPeriod) {
                val endDateByWeek =
                    fmt.parseDateTime(monthlyPlanList[i].startDate).plusDays(j)

                /**
                 *  일정에 열의 마지막 날에 해당하는 토요일이 포함된다면 endDate로 잡기
                 */
                for (d in 0 until weekLastDayList.size) {
                    if (endDateByWeek.toString(fmtOut).equals(weekLastDayList[d])) {
                        planSet = PlanSet(
                            monthlyPlanList[i].id,
                            monthlyPlanList[i].title,
                            startDateByWeek.toString(fmtOut),
                            endDateByWeek.toString(fmtOut)
                        )
                        startDateByWeek = endDateByWeek.plusDays(1)
                        setWeeklyPlanList(planSet)
                        continue@loop
                    }
                }

                /**
                 * 일정의 마지막 날짜를 row의 endDate로 잡는다.
                 */
                if (j == planPeriod - 1) {
                    planSet = PlanSet(
                        monthlyPlanList[i].id,
                        monthlyPlanList[i].title,
                        startDateByWeek.toString(fmtOut),
                        endDateByWeek.toString(fmtOut)
                    )
                    setWeeklyPlanList(planSet)
                    continue@loop
                }
            }
        }

    }

    private fun setWeeklyPlanList(plan: PlanSet) {
        /**
         * childView에 하나의 rectangle로 잡을 리스트를 add 시킨다
         */
        val offSetPlanList = createPlanSetList()
        if (plan.id != -1) {

            val index  // add 시킬 일정이 몇주차에 추가될지에 대한 editList index
                    = getWeekOfMonth(fmtOut.parseDateTime(plan.startDate)) - 1

            /**
             * editList는 2차배열로 되어있는데 재편집을 위해서
             * editList[index]에 있는 배열들을 다시 testList에 추가한 후
             * 마지막 추가시킬 planSet 을 추가한다
             *
             * testList = editList[index] + planSet
             * editList[index] = testList
             */
            for (l in 0 until weeklyPlanList[index].size) {
                offSetPlanList.add(weeklyPlanList[index][l])
            }

            offSetPlanList.add(plan)
            weeklyPlanList[index] = offSetPlanList

            planSet = PlanSet(-1, "", null, null)
        }
    }

    private fun createPlanSetList(): ArrayList<PlanSet> {
        return ArrayList()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("jyl", "$this: onMeasure")
        val h = paddingTop + paddingBottom + max(
            suggestedMinimumHeight,
            (_height * WEEKS_PER_MONTH).toInt()
        )
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d("jyl", "${this.javaClass.name}: onLayout")
        iWidth = (width / DAYS_PER_WEEK).toFloat()
        iHeight = (height / WEEKS_PER_MONTH).toFloat()

        initRectChildView()

        /**
         * 몇 주차 인지에 대한 list 의 인덱스값
         * (index 0: 1주차 ~ index 5: 6주차)
         */
        var curWeekIndex = 0

        /**
         * 한 주차에 대한 영역
         */
        children.forEach { view ->

            val topMargin = -5
            if (curWeekIndex >= weeklyPlanList.size) {
                return
            }

            val row = weeklyPlanList[curWeekIndex].size   // 한주차에서 필요한 열 개수 반환

            val rowHeight = 240    // 열이 늘어날 때마다 필요한 높이
            view.layout(
                0,
                ((curWeekIndex - 1) * iHeight.toInt()) + topMargin,
                width,
                ((curWeekIndex - 1) * iHeight.toInt()) + topMargin + (row * rowHeight)
            )
            curWeekIndex++
        }
    }

    private fun initRectChildView() {
        Log.d("jyl", "${this.javaClass.name}: iniRect")
        var index = 0
        weeklyPlanList.forEach { _ ->
            var h = iHeight
            var w = iWidth
            addView(
                RectChildView(
                    context = context,
                    weeklyPlanList = weeklyPlanList[index],
                    iHeight = h,
                    iWidth = w
                )
            )
            index++
        }
    }
}