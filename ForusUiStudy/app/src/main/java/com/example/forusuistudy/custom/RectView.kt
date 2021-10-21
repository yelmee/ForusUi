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
    private val nullList = ArrayList<PlanSet>()
    private val nullPlan = PlanSet(-1, "null", "null", "null")
    private var editList = ArrayList<ArrayList<PlanSet>>()
    private var planSet = PlanSet(-1, "", null, null)
    private val saturdayList = arrayListOf<String>() // 주의 마지막 요일인 토요일에 해당되는 날짜의 리스트

    private val fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
    private val fmtOut = DateTimeFormat.forPattern("yyyy-MM-dd")

    private var _height: Float = 0f
    private var iHeight = 0F
    private var iWidth = 0F

    private val allDay = WEEKS_PER_MONTH * DAYS_PER_WEEK
    private val firstDayOfMonth = DateTime().withDayOfMonth(1)
    private val prevOffSet = getPrevOffSet(firstDayOfMonth)
    private val firstDayOfCalendar = firstDayOfMonth.minusDays(prevOffSet)
    private val saturdayNum = "6"

    companion object {
        var originList = ArrayList<Plan>()
        private var onDismissListener: ((Plan) -> Unit)? = null
    }

    init {
        Log.d("jyl", "$this: init")
//        setViewModel(data)

        nullList.add(nullPlan)
        PlanAddDialog.onDismissListener = onDismissListener
        onDismissListener = { plan ->
            Log.d("jyl", "$this: onDismissListener")

            originList.add(plan)
            initEditList()
            editListToWeekSchedule()
            requestLayout()
        }

        setWillNotDraw(false)
        context.withStyledAttributes(attrs, R.styleable.RectView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.RectView_rectHeight, 0f)
        }

        initOriginList()
        initEditList()
        setSaturdayListOfThisMonth()
        editListToWeekSchedule()
    }

    private fun initOriginList() {
        originList = ArrayList<Plan>()
        var data = Plan(1, "알고리즘 공부", addTime(10), addTime(11), "www.naver.com", 12, 80)
        originList.add(data)

//        data = Plan(1, "안드로이드 개념 공부", addTime(11), addTime(20), "www.naver.com", 12, 80)
//        originList.add(data)
//        data = Plan(1, "디자인 그리기", addTime(17), addTime(19), "www.naver.com", 12, 80)
//        originList.add(data)

    }

    private fun initEditList() {
        /**
         * 
         */
        editList = ArrayList<ArrayList<PlanSet>>()
        for (i in 0..5) {
            editList.add(nullList)
        }
    }

    private fun setSaturdayListOfThisMonth() {
        /**
         * 선택된 날짜의 월에 해당하는 토요일 일자를 리스트로 담기
         */
        for (i in 0 until allDay) {
            val curDateDayOfWeek = firstDayOfCalendar.plusDays(i).dayOfWeek().asString
            if (curDateDayOfWeek.equals(saturdayNum)) {
                saturdayList.add(firstDayOfCalendar.plusDays(i).toString(fmtOut))
            }
        }
    }

    private fun editListToWeekSchedule() {

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

                /**
                 *  일정에 열의 마지막 날에 해당하는 토요일이 포함된다면 endDate로 잡기
                 */
                for (d in 0 until saturdayList.size) {
                    if (curDate.toString(fmtOut).equals(saturdayList[d])) {
                        planSet = PlanSet(
                            originList[i].id,
                            originList[i].title,
                            firstDay.toString(fmtOut),
                            curDate.toString(fmtOut)
                        )
                        firstDay = curDate.plusDays(1)
                    }
                }

                /**
                 * 일정의 마지막 날짜를 row의 endDate로 잡는
                 */
                if (j == period - 1) {
                    planSet = PlanSet(
                        originList[i].id,
                        originList[i].title,
                        firstDay.toString(fmtOut),
                        curDate.toString(fmtOut)
                    )
                }

                /**
                 * childView에 하나의 rectangle로 잡을 리스트를 add 시킨다
                 */
                val testList = ArrayList<PlanSet>()
                if (planSet.id != -1) {

                    val index  // add 시킬 일정이 몇주차에 추가될지에 대한 editList index
                    = getWeekOfMonth(fmtOut.parseDateTime(planSet.startDate)) - 1

                    /**
                     * editList는 2차배열로 되어있는데 재편집을 위해서
                     * editList[index]에 있는 배열들을 다시 testList에 추가한 후
                     * 마지막 추가시킬 planSet 을 추가한다
                     *
                     * testList = editList[index] + planSet
                     * editList[index] = testList
                     */
                    for (l in 0 until editList[index].size) {
                        testList.add(editList[index][l])
                    }

                    testList.add(planSet)
                    editList[index] = testList
                    planSet = PlanSet(-1, "", null, null)
                }
            }
//            Log.d("jyl", "$this: list: $editList")
        }
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
        Log.d("jyl", "$this: onLayout")
        iWidth = (width / DAYS_PER_WEEK).toFloat()
        iHeight = (height / WEEKS_PER_MONTH).toFloat()

        initRect()

        /**
         * 몇 주차 인지에 대한 list 의 인덱스값
         * (index 0: 1주차 ~ index 5: 6주차)
         */
        var currentWeekIndex = 0

        /**
         * 한 주차에 대한 영역
         */
        children.forEach { view ->

            invalidate()
            requestLayout()
            val topMargin = -5
            if (currentWeekIndex >= editList.size) {
                return
            }

//            val row = editList[currentWeekIndex].size   // 한주차에서 필요한 열 개수 반환
            val row = 2   // 한주차에서 필요한 열 개수 반환

            val rowHeight = 240    // 열이 늘어날 때마다 필요한 높이

            view.layout(
                0,
                ((currentWeekIndex - 1) * iHeight.toInt()) + topMargin,
                width,
                ((currentWeekIndex - 1) * iHeight.toInt()) + topMargin + (row * rowHeight)
            )
            currentWeekIndex++
        }
    }

    private fun initRect() {
        var index = 0
        editList.forEach { _ ->
            var h = iHeight
            var w = iWidth
            addView(
                RectChildView(
                    context = context,
                    lili = editList[index],
                    iHeight = h,
                    iWidth = w,
                    index = index
                )
            )
            index++
        }
    }

//    private fun setViewModel(data: Plan) {
//        binding.planData = data
//    }
}