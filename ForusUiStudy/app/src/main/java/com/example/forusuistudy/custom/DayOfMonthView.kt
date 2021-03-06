package com.example.forusuistudy.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.example.forusuistudy.R
import com.example.forusuistudy.utils.CalendarUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import kotlin.math.max

/**
 *Created By Yelim ON 2021/10/14
 */
class DayOfMonthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.dayViewStyle,
    @StyleRes defStyleRes: Int = R.style.Day_DayViewStyle
) : ViewGroup(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private var _height: Float = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.DayView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.DayView_dayOfMonthHeight, 0f)
        }
    }

    /**
     * Measure
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = paddingTop + paddingBottom + max(suggestedMinimumHeight, (_height ).toInt())
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), h)
    }

    /**
     * Layout
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val iWidth = (width / DateTimeConstants.DAYS_PER_WEEK).toFloat()
        val iHeight = (height).toFloat()
        var index = 0
        children.forEach { view ->
            val left = (index % DateTimeConstants.DAYS_PER_WEEK) * iWidth
            val top = (index / DateTimeConstants.DAYS_PER_WEEK) * iHeight

            view.layout(left.toInt(), top.toInt(), (left + iWidth).toInt(), (top + iHeight).toInt())

            index++
        }
    }

    /**
     * ?????? ????????? ????????????.
     * @param firstDayOfMonth   ??? ?????? ?????? ??????
     * @param list              ????????? ????????? ?????? ????????? ????????? ?????? (??? 42???)
     */
    fun initCalendar() {
        val listDayOfMonth = listOf("???", "???","???","???","???","???","???")
        var index = 0

        listDayOfMonth.forEach {
            addView(DayView(
                context = context,
                day = listDayOfMonth[index]
            ))

            index++
        }
    }


}