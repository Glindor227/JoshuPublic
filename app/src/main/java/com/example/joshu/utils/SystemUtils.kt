package com.example.joshu.utils

import java.text.SimpleDateFormat
import java.util.*

object SystemUtils {
    fun formatDate(time: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        return dateFormat.format(Date(time))
    }
}