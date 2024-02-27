package com.kien.petclub.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun convertMillisToDate(): String {
    val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val calender = Calendar.getInstance()
    calender.timeInMillis = System.currentTimeMillis()
    return format.format(calender.time)
}