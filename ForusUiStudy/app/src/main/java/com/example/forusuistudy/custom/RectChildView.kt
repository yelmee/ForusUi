package com.example.forusuistudy.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import com.example.forusuistudy.R
import com.example.forusuistudy.data.PlanSet
import com.example.forusuistudy.data.PlanWithRow
import com.example.forusuistudy.utils.CalendarUtils.Companion.changeStringToDate
import com.example.forusuistudy.utils.CalendarUtils.Companion.isOverlappingDate
import org.joda.time.format.DateTimeFormat

class RectChildView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.rectViewStyle,
    @StyleRes defStyleRes: Int = R.style.Rect_rectShape,
    private val weeklyPlanList: ArrayList<PlanSet>,
    private val iWidth: Float,
    private val iHeight: Float,
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {
    private val bounds = Rect()
    private val paint = Paint()
    private var paintText = Paint()

    init {
        context.withStyledAttributes(attrs, R.styleable.RectView, defStyleAttr, defStyleRes) {
            val planRectSize = getString(R.styleable.RectView_termFrom)

            paint.apply {
                color = context.resources.getColor(R.color.calendarYellow)
                strokeWidth = 3F
            }

            /* 흰색 배경에 유색 글씨 */
            paintText = TextPaint().apply {
                isAntiAlias = true
                textSize = 25f
                color = Color.WHITE
            }
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var weeklyList = weeklyPlanList

        if (canvas == null) return

        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd")

        var left = 0F
        var right = 0F
        var top = 0F
        var bottom = 0F
        var startDayOfWeek = 0
        var endDayOfWeek = 0

        var drawnPlanListOfWeek = arrayListOf<PlanWithRow>()
        for (i in 1 until weeklyList.size) {

            startDayOfWeek =
                ifSunDaySetWeekNumToZero(fmt.parseDateTime(weeklyList[i].startDate).dayOfWeek)    // 시작일의 요일인덱스(0: 일요일 ~ 6: 토요일)
            endDayOfWeek =
                ifSunDaySetWeekNumToZero(fmt.parseDateTime(weeklyList[i].endDate).dayOfWeek)       // 마감일의 요일인덱스

            val rectHeight = 50F // 사각형 간격
            val rectInterval = 60F  // 사각형 높이
            val d = 20  // 높이 오류줄임

            var isThisRowOverlap = true
            var currentRowIndex = 1 // 현재 검사하고 있는 row

            /**
             * 이미 그려진 일정과 겹치지 않게 검사한다
             */
            for (j in 0 until drawnPlanListOfWeek.size) {

                val dateStart1 = changeStringToDate(weeklyList[i].startDate!!)
                val dateEnd1 = changeStringToDate(weeklyList[i].endDate!!)

                val dateStart2 = changeStringToDate(drawnPlanListOfWeek[j].startDate!!)
                val dateEnd2 = changeStringToDate(drawnPlanListOfWeek[j].endDate!!)

                val isRowChanged = currentRowIndex != drawnPlanListOfWeek[j].row  // 이전 row 검사가 끝났을 때 true
                isThisRowOverlap =
                    isOverlappingDate(dateStart1, dateEnd1, dateStart2, dateEnd2) // date 검사

                /**
                 * 1. 이전 row 검사가 끝났으며, overlap 된 날짜가 없을 때,
                 *    또는 마지막 그려질 일정이며, overlap 된 날짜가 없을 때,
                 *      -> 이전 row의 y좌표에 일정 그림
                 *
                 * 2. drawnList row의 마지막 index에 날짜를 추가
                 */
                if (!isThisRowOverlap && isRowChanged
                    || !isThisRowOverlap && drawnPlanListOfWeek.size == (j + 1)) {

                    top = iHeight + (currentRowIndex * rectHeight) + d
                    bottom = iHeight + rectInterval + (currentRowIndex * rectHeight)
                    // ???!!!! 설명 필요
                    if (isRowChanged) {
                        currentRowIndex++
                    }
                    val planWithRow = PlanWithRow(
                        weeklyList[i].id,
                        weeklyList[i].title,
                        weeklyList[i].startDate,
                        weeklyList[i].endDate,
                        currentRowIndex
                    )
                    drawnPlanListOfWeek.add(j, planWithRow)

                    /**
                     * overlap된 날짜가 있으며, 마지막 그려질 일정일 때
                     * -> 다음 row의 y좌표에 일정 그림
                     */
                } else if (isThisRowOverlap && drawnPlanListOfWeek.size == (j + 1)) {
                    top = iHeight + ((currentRowIndex + 1) * rectHeight) + d
                    bottom = iHeight + rectInterval + ((currentRowIndex + 1) * rectHeight)

                    val planWithRow = PlanWithRow(
                        weeklyList[i].id,
                        weeklyList[i].title,
                        weeklyList[i].startDate,
                        weeklyList[i].endDate,
                        currentRowIndex + 1
                    )
                    drawnPlanListOfWeek.add(j + 1, planWithRow)

                }
                /**
                 * 검사할 row가 변경되었을 때 currentRowIndex count를 1올려줌
                 */
                if (isRowChanged) {
                    currentRowIndex++
                }
            }

            /**
             * 비교할 일정이 없으며, 처음 일정을 그려야할 때 검사없이 drawnList 에 일정 추가
             */
            if (drawnPlanListOfWeek.size == 0) {
                top = iHeight + (currentRowIndex * rectHeight) + d
                bottom = iHeight + rectInterval + (currentRowIndex * rectHeight)

                val planWithRow = PlanWithRow(
                    weeklyList[i].id,
                    weeklyList[i].title,
                    weeklyList[i].startDate,
                    weeklyList[i].endDate,
                    currentRowIndex
                )
                drawnPlanListOfWeek.add(planWithRow)
            }

            left = (startDayOfWeek) * iWidth
            right = (endDayOfWeek + 1) * iWidth

            canvas.drawRect(left, top, right, bottom, paint)
            val title = weeklyList[i].title

            paintText.getTextBounds(title, 0, title.length, bounds)
            canvas.drawText(
                title,
                left,
                bottom - (bounds.height() / 2),
                paintText
            )
            left = 0F
            right = 0F
            top = 0F
            bottom = 0F
            startDayOfWeek = -1
            endDayOfWeek = -1
        }
    }

    private fun ifSunDaySetWeekNumToZero(week: Int): Int {
        if (week == 7) {
            return 0
        }
        return week
    }
}