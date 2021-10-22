package com.example.forusuistudy.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import com.example.forusuistudy.OnDismissListener
import com.example.forusuistudy.R
import com.example.forusuistudy.data.Plan
import com.example.forusuistudy.data.PlanSet
import com.example.forusuistudy.data.PlanWithRow
import com.example.forusuistudy.dialog.PlanAddDialog
import com.example.forusuistudy.utils.CalendarUtils.Companion.changeStringToDate
import com.example.forusuistudy.utils.CalendarUtils.Companion.getLogOfArray
import com.example.forusuistudy.utils.CalendarUtils.Companion.isOverlappingDate
import org.joda.time.format.DateTimeFormat

class RectChildView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.rectViewStyle,
    @StyleRes defStyleRes: Int = R.style.Rect_rectShape,
    private val lili: ArrayList<PlanSet>,
    private val iWidth: Float,
    private val iHeight: Float,
    private val index: Int
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {
    private val bounds = Rect()
    private val paint = Paint()
    private var paintText = Paint()
    var indexx = 1

    companion object {
        private var onDrawListener: (() -> Unit)? = null
    }
    init {
        RectView.onDrawListener = onDrawListener
        onDrawListener = {
            Log.d("jyl", "${this.javaClass.name}: onDismissListener")
            indexx = 1000
            invalidate()
            requestLayout()
        }

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

        var list = lili
        indexx += 1

        Log.d("jyl", "RectChildView: ${index} ${lili}")

        if (canvas == null) return

        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd")

        var left = 0F
        var right = 0F
        var top = 0F
        var bottom = 0F
        var startDayOfWeek = 0
        var endDayOfWeek = 0

        var drawnList = arrayListOf<PlanWithRow>()
        for (i in 1 until list.size) {

            startDayOfWeek =
                ifSunDaySetWeekNumToZero(fmt.parseDateTime(list[i].startDate).dayOfWeek)    // 시작일의 요일인덱스(0: 일요일 ~ 6: 토요일)
            endDayOfWeek =
                ifSunDaySetWeekNumToZero(fmt.parseDateTime(list[i].endDate).dayOfWeek)       // 마감일의 요일인덱스

            val rectHeight = 50F // 사각형 간격
            val rectInterval = 60F  // 사각형 높이
            val d = 20  // 높이 오류줄임

            var isThisRowOverlap = true
            var currentRowIndex = 1 // 현재 검사하고 있는 row

            /**
             * 이미 그려진 일정과 겹치지 않게 검사한다
             */
            for (j in 0 until drawnList.size) {

                val dateStart1 = changeStringToDate(list[i].startDate!!)
                val dateEnd1 = changeStringToDate(list[i].endDate!!)

                val dateStart2 = changeStringToDate(drawnList[j].startDate!!)
                val dateEnd2 = changeStringToDate(drawnList[j].endDate!!)


                val isRowChanged = currentRowIndex != drawnList[j].row  // 이전 row 검사가 끝났을 때 true
                isThisRowOverlap =
                    isOverlappingDate(dateStart1, dateEnd1, dateStart2, dateEnd2) // date 검사

                /**
                 * 이전 row 검사가 끝났으며, overlap 된 날짜가 없을 때 이전 row의 y좌표에 일정 그림
                 * drawnList row의 마지막 index에 날짜를 추가
                 */
                if (!isThisRowOverlap && isRowChanged
                    || !isThisRowOverlap && drawnList.size == (j + 1)
                ) {

                    top = iHeight + (currentRowIndex * rectHeight) + d
                    bottom = iHeight + rectInterval + (currentRowIndex * rectHeight)

                    val planWithRow = PlanWithRow(
                        list[i].id,
                        list[i].title,
                        list[i].startDate,
                        list[i].endDate,
                        currentRowIndex
                    )
                    drawnList.add(j + 1, planWithRow)
                } else if (isThisRowOverlap && drawnList.size == (j + 1)) {
                    top = iHeight + ((currentRowIndex + 1) * rectHeight) + d
                    bottom = iHeight + rectInterval + ((currentRowIndex + 1) * rectHeight)

                    val planWithRow = PlanWithRow(
                        list[i].id,
                        list[i].title,
                        list[i].startDate,
                        list[i].endDate,
                        currentRowIndex + 1
                    )
                    drawnList.add(j + 1, planWithRow)

                } else {

                }
                /**
                 * 검사할 row가 변경되었을 때 currentRowIndex count를 1올려줌
                 */
                if (isRowChanged) {
                    currentRowIndex++
                }
            }

            if (drawnList.size == 0) {
                top = iHeight + (currentRowIndex * rectHeight) + d
                bottom = iHeight + rectInterval + (currentRowIndex * rectHeight)

                val planWithRow = PlanWithRow(
                    list[i].id,
                    list[i].title,
                    list[i].startDate,
                    list[i].endDate,
                    currentRowIndex
                )
                drawnList.add(planWithRow)
            }

            left = (startDayOfWeek) * iWidth
            right = (endDayOfWeek + 1) * iWidth

//            getLogOfArray(drawnList)
            Log.d("jyl", "$this: drawRect")

            canvas.drawRect(left, top, right, bottom, paint)
//            canvas.drawRect(0F, 0F, 100F, 100F, paint)
            val title = list[i].title

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