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
import com.example.forusuistudy.adapter.CalendarAdapter
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
    private var weekLastDayList = arrayListOf<String>() // ?????? ????????? ????????? ???????????? ???????????? ????????? ?????????

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
        var onCreateCalendarCallback:((String) -> Unit)? = null
    }

    init {
        setWillNotDraw(false)
        context.withStyledAttributes(attrs, R.styleable.RectView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.RectView_rectHeight, 0f)
        }
        emptyList.add(emptyPlan)

        CalendarAdapter.CreateCalendarCallback = onCreateCalendarCallback
        onCreateCalendarCallback = { month ->
            Log.d("jyl", "${this.javaClass.name}: $month")
            initYearlyPlanList()
            initWeeklyPlanList()
            setWeekLastDaysOfMonth()
            firstDayOfMonth = fmt4.parseDateTime(month).withDayOfMonth(1)
            monthlyPlanList = filterMonthlyPlan(month, yearlyPlanList)
            editMonthlyPlanToWeekPlan()
            removeAllViews()
        }


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
        var data = Plan(1, "??????1", addTime(5, 0), addTime(7, 0), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "??????2", addTime(22, 1), addTime(23, 1), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "??????3", addTime(22, 1), addTime(29, 1), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "??????4", addTime(18, 0), addTime(21, 0), "www.naver.com", 12, 80)
        yearlyPlanList.add(data)

        data = Plan(1, "??????5", addTime(17, 0), addTime(18, 0), "www.naver.com", 12, 80)
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
         * ????????? ????????? ?????? ???????????? ????????? ????????? ???????????? ??????
         */
        weekLastDayList = arrayListOf<String>()
        for (i in 0 until allDay) {
             val curDateDayOfWeek = firstDayOfCalendar.plusDays(i).dayOfWeek().asString
            if (curDateDayOfWeek.equals(lastDayOfWeekIndex)) {
                weekLastDayList.add(firstDayOfCalendar.plusDays(i).toString(fmtOut))
            }
        }
        Log.d("jyl", "${this.javaClass.name}: ${weekLastDayList.toList()}")
    }

    private fun editMonthlyPlanToWeekPlan() {

        /**
         * ?????? ???????????? ?????? ?????? ???????????? ?????????
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
                 *  ????????? ?????? ????????? ?????? ???????????? ???????????? ??????????????? endDate??? ??????
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
                 * ????????? ????????? ????????? row??? endDate??? ?????????.
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
        Log.d("jyl", "${this.javaClass.name}: edited ${weeklyPlanList.toList()}")
        removeAllViews()
    }

    private fun setWeeklyPlanList(plan: PlanSet) {
        /**
         * childView??? ????????? rectangle??? ?????? ???????????? add ?????????
         */
        val offSetPlanList = createPlanSetList()
        if (plan.id != -1) {

            val index  // add ?????? ????????? ???????????? ??????????????? ?????? editList index
                    = getWeekOfMonth(fmtOut.parseDateTime(plan.startDate)) - 1

            /**
             * editList??? 2???????????? ??????????????? ???????????? ?????????
             * editList[index]??? ?????? ???????????? ?????? testList??? ????????? ???
             * ????????? ???????????? planSet ??? ????????????
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
        Log.d("jyl", "${this.javaClass.name}: onMeasure")
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
         * ??? ?????? ????????? ?????? list ??? ????????????
         * (index 0: 1?????? ~ index 5: 6??????)
         */
        var curWeekIndex = 0

        /**
         * ??? ????????? ?????? ??????
         */
        children.forEach { view ->

            val topMargin = -5
//            if (curWeekIndex >= weeklyPlanList.size) {
//                return
//            }

            val row = weeklyPlanList[curWeekIndex].size   // ??????????????? ????????? ??? ?????? ??????

            val rowHeight = 240    // ?????? ????????? ????????? ????????? ??????
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
        Log.d("jyl", "${this.javaClass.name}: ${weeklyPlanList.toList()}")
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