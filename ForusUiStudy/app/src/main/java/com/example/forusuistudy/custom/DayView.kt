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
import com.example.forusuistudy.R

/**
 *Created By Yelim ON 2021/10/14
 */

class DayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.Calendar_ItemViewStyle,
    private val day: String
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {
    private val bounds = Rect()
    private val paint = Paint()
    private var paintText = Paint()

    init {
        paint.apply {
            color = Color.BLACK
            strokeWidth = 1F
            style = Paint.Style.STROKE
        }

        paintText = TextPaint().apply {
            color = Color.BLACK
            isAntiAlias = true
            textSize = 30F
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val bottom = height
        val right = width
        val rect = Rect(1, 1, right, bottom)
        canvas.drawRect(rect, paint)

        paint.getTextBounds(day, 0, day.length, bounds)
        canvas.drawText(
            day,
            ((width / 2 - bounds.width() / 2) - 2).toFloat(),
            (height / 2 + bounds.height()).toFloat(),
            paintText
        )
    }
}