package com.example.joshu.mvp.model

import java.util.*

interface IDateFormatter {
    fun getTimeShort(dateTimeInSec: Long): String
    fun getDateLongOfCalendar(calendar: Calendar): String
    fun getDateFullOfCalendar(calendar: Calendar): String
    fun getTimeShortOfCalendar(calendar: Calendar): String
    fun getDateTimeShot(dateTimeInMillis: Long): String
    fun getDayMonthFull(dateTimeInMillis: Long): String
    fun getTodayWithTime(hours: Int, minutes: Int, seconds: Int, milliseconds: Int): Long
    fun getTimeShortFromMillis(dateTimeInMillis: Long): String
}