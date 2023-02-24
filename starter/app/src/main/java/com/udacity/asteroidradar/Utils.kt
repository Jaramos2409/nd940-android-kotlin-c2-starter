package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getSevenDaysFromToday(): Date {
    val calendarSevenDaysFromNow = Calendar.getInstance()
    calendarSevenDaysFromNow.add(Calendar.DATE, 7)
    return calendarSevenDaysFromNow.time
}