package com.example.forusuistudy.custom

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import com.example.forusuistudy.R
import com.example.forusuistudy.utils.CalendarUtils.Companion.getDateColor
import com.example.forusuistudy.utils.CalendarUtils.Companion.isSameMonth
import org.joda.time.DateTime

class DayItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.Calendar_ItemViewStyle,
    private val date: DateTime = DateTime(),
    private val firstDayOfMonth: DateTime = DateTime()
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private val bounds = Rect()
    private var paint: Paint = Paint()
    private var paintBorder: Paint = Paint()

    init {
        /* Attributes */
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            val dayTextSize =
                getDimensionPixelSize(R.styleable.CalendarView_dayTextSize, 0).toFloat()

            /* 흰색 배경에 유색 글씨 */
            paint = TextPaint().apply {
                isAntiAlias = true
                textSize = dayTextSize
                color = getDateColor(date.dayOfWeek)
                if (!isSameMonth(date, firstDayOfMonth)) {
                    alpha = 50
                }
            }

            paintBorder.apply {
                color = Color.BLACK
                strokeWidth = 1F
                style = Paint.Style.STROKE
//                val color = Color.BLACK
//                colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_OUT)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val date = date.dayOfMonth.toString()
        paint.getTextBounds(date, 0, date.length, bounds)
        canvas.drawText(
            date,
            0F + 14F,
            bounds.height().toFloat() + 14F,
            paint
        )
        val top = 1
        val left = 1
        val bottom = height
        val right = width
         var rect = Rect(top, left, right, bottom)
        canvas.drawRect(rect, paintBorder)
    }
}