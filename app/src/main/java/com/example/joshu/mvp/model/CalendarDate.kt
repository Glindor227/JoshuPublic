package com.example.joshu.mvp.model

data class CalendarDate (
    val day: Int,
    val month: Int,
    val year: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CalendarDate

        if (day != other.day) return false
        if (month != other.month) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = day
        result = 31 * result + month
        result = 31 * result + year
        return result
    }
}