package com.kien.petclub.utils

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun convertMillisToDate(): String {
    val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val calender = Calendar.getInstance()
    calender.timeInMillis = System.currentTimeMillis()
    return format.format(calender.time)
}

fun readJsonFromAssets(context: Context, fileName: String): String? {
    return try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
}