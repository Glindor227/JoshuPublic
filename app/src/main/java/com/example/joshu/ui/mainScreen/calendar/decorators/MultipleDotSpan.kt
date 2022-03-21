package com.example.joshu.ui.mainScreen.calendar.decorators

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan


class MultipleDotSpan(private val radius: Float,
                      private val colors: List<Int>)
    : LineBackgroundSpan {

    override fun drawBackground(
            canvas: Canvas, paint: Paint,
            left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
            text: CharSequence, start: Int, end: Int, lineNumber: Int) {

        val total = colors.size
        if (total > 1) {
            var leftMost = (total - 1) * -5

            for (i in 0 until total) {
                val oldColor = paint.color

                paint.color = colors.elementAt(i)

                canvas.drawCircle(
                        ((left + right) / 2 - leftMost).toFloat(),
                        (bottom + radius),
                        radius, paint)

                paint.color = oldColor
                leftMost += 10
            }
        } else {
            canvas.drawCircle(
                    ((left + right) / 2).toFloat(),
                    (bottom + radius),
                    radius, paint)
        }
    }
}