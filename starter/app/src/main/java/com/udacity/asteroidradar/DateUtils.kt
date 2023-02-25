package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getDateOfTodayStringFormatted(format: String = "yyyy-MM-dd"): String {
    return Calendar.getInstance().time.toString(format)
}

fun getDateOfTomorrowStringFormatted(format: String = "yyyy-MM-dd"): String {
    val calendarSevenDaysFromNow = Calendar.getInstance()
    calendarSevenDaysFromNow.add(Calendar.DATE, 1)
    return calendarSevenDaysFromNow.time.toString(format)
}

fun getDateOfSevenDaysFromTodayStringFormatted(format: String = "yyyy-MM-dd"): String {
    val calendarSevenDaysFromNow = Calendar.getInstance()
    calendarSevenDaysFromNow.add(Calendar.DATE, 7)
    return calendarSevenDaysFromNow.time.toString(format)
}

fun getDateOfEightDaysFromTodayStringFormatted(format: String = "yyyy-MM-dd"): String {
    val calendarSevenDaysFromNow = Calendar.getInstance()
    calendarSevenDaysFromNow.add(Calendar.DATE, 8)
    return calendarSevenDaysFromNow.time.toString(format)
}