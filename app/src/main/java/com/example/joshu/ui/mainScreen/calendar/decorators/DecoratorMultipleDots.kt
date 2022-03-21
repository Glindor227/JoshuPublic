package com.example.joshu.ui.mainScreen.calendar.decorators

import com.example.joshu.mvp.model.CalendarDate
import com.example.joshu.utils.ViewsUtil
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class DecoratorMultipleDots(
        private val dates: Set<CalendarDate>,
        private val colors: List<Int>) : DayViewDecorator {

    companion object {
        private const val DOT_RADIUS = 2
    }

    override fun shouldDecorate(day: CalendarDay): Boolean =
            dates.contains(convertCalendarDayToCalendarDate(day))

    override fun decorate(view: DayViewFacade) {
        val radius = ViewsUtil.dpToPx(DOT_RADIUS).toFloat()

        if (colors.size > 1) {
            view.addSpan(MultipleDotSpan(radius, colors))
        } else if (colors.size == 1) {
            view.addSpan(DotSpan(radius, colors[0]))
        }
    }

    private fun convertCalendarDayToCalendarDate(day: CalendarDay): CalendarDate {
        return CalendarDate(day.day, day.month, day.year)
    }
}