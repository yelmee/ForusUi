package com.leveloper.infinitecalendar.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import com.example.forusuistudy.R
import com.leveloper.infinitecalendar.CalendarFragment
import com.leveloper.infinitecalendar.data.PlanSet
import com.leveloper.infinitecalendar.utils.CalendarUtils
import org.joda.time.DateTimeConstants.DAYS_PER_WEEK
import org.joda.time.format.DateTimeFormat

class RectChildView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.rectViewStyle,
    @StyleRes defStyleRes: Int = R.style.Rect_rectShape,
    private val list: ArrayList<PlanSet>,
    private val iWidth: Float,
    private val iHeight: Float
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {
    private val bounds = Rect()
    private val paint = Paint()

    init {
        context.withStyledAttributes(attrs, R.styleable.RectView, defStyleAttr, defStyleRes) {
            val planRectSize = getString(R.styleable.RectView_termFrom)

            paint.apply {
                color = Color.BLUE
                strokeWidth = 3F
            }
        }
    }

    private fun ifSunDaySetWeekNumToZero(week: Int): Int {
        if (week == 7) {
            return 0
        }
        return week
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = (iWidth / DAYS_PER_WEEK).toFloat()
        val height = (iHeight / CalendarUtils.WEEKS_PER_MONTH).toFloat()
        val prevCount = CalendarFragment.prevCount

        if (canvas == null) return

        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd")

        var left = 0F
        var right = 0F
        var top = 0F
        var bottom = 0F
        var startDayOfWeek = 0
        var endDayOfWeek = 0

        for (i in 1 until list.size) {
            startDayOfWeek = ifSunDaySetWeekNumToZero(fmt.parseDateTime(list[i].termFrom).dayOfWeek)
            endDayOfWeek = ifSunDaySetWeekNumToZero(fmt.parseDateTime(list[i].termTo).dayOfWeek)

            val rectHeight = 10F
            val rectInterval = 5F
            left = (startDayOfWeek) * iWidth
            right = (endDayOfWeek + 1) * iWidth
            top =  iHeight +  ( i * rectHeight )
            bottom =  iHeight + rectInterval + ( i * rectHeight)

            canvas.drawRect(left, top, right, bottom, paint)

            left = 0F
            right = 0F
            top = 0F
            bottom = 0F
            startDayOfWeek = -1
            endDayOfWeek = -1
        }

    }
}