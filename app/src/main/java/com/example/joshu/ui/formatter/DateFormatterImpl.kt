package com.example.joshu.ui.formatter

import com.example.joshu.mvp.model.IDateFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateFormatterImpl : IDateFormatter {
    override fun getTimeShort(dateTimeInSec: Long): String {
        val date = Calendar.getInstance()
        date.timeInMillis = dateTimeInSec * 1000
        return getTimeShortOfCalendar(date)
    }

    override fun getDateLongOfCalendar(calendar: Calendar): String {
        return DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
            .format(calendar.time)
    }

    override fun getDateFullOfCalendar(calendar: Calendar): String {
        return DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault())
            .format(calendar.time)
    }

    override fun getTimeShortOfCalendar(calendar: Calendar): String {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
    }

    override fun getDateTimeShot(dateTimeInMillis: Long): String {
        val date = Calendar.getInstance()
        date.timeInMillis = dateTimeInMillis
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date.time)
    }

    override fun getDayMonthFull(dateTimeInMillis: Long): String {
        val date = Calendar.getInstance()
        date.timeInMillis = dateTimeInMillis
        val dayMonth = SimpleDateFormat("dd MMMM", Locale.getDefault())
        return dayMonth.format(date.time)
    }

    override fun getTodayWithTime(hours: Int, minutes: Int, seconds: Int, milliseconds: Int): Long {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.HOUR_OF_DAY, hours)
        currentDate.set(Calendar.MINUTE, minutes)
        currentDate.set(Calendar.SECOND, seconds)
        currentDate.set(Calendar.MILLISECOND, milliseconds)
        return currentDate.timeInMillis
    }

    override fun getTimeShortFromMillis(dateTimeInMillis: Long): String {
        val date = Calendar.getInstance()
        date.timeInMillis = dateTimeInMillis
        return getTimeShortOfCalendar(date)
    }

}