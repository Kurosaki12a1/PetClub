package com.kien.petclub.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kien.petclub.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun showMessage(context: Context, message: String?) {
    Toast.makeText(
        context,
        message ?: context.resources.getString(R.string.some_error),
        Toast.LENGTH_SHORT
    )
        .show()
}

/*fun showNoInternetAlert(activity: Activity) {
    Alerter.create(activity)
        .setTitle(activity.resources.getString(R.string.connection_error))
        .setText(activity.resources.getString(R.string.no_internet))
        .setIcon(R.drawable.ic_no_internet)
        .setBackgroundColorRes(R.color.red)
        .enableClickAnimation(true)
        .enableSwipeToDismiss()
        .show()
}*/



fun convertMillisToDate(): String {
    val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val calender = Calendar.getInstance()
    calender.timeInMillis = System.currentTimeMillis()
    return format.format(calender.time)
}