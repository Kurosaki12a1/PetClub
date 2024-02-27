package com.kien.petclub.extensions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat

fun <A : Activity> Activity.openActivityAndClearStack(activity: Class<A>) {
    Intent(this, activity).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(this)
        finish()
    }
}

fun <A : Activity> Activity.openActivity(
    activity: Class<A>,
    vararg extras: Pair<String, Any?>,
    flags: Int? = null
) {
    Intent(this, activity).apply {
        extras.forEach { (key, value) ->
            when (value) {
                is Int -> putExtra(key, value)
                is String -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
            }
        }
        flags?.let { addFlags(it) }
        startActivity(this)
    }
}


fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.checkAndRequestPermission(permission: String, requestCode: Int) {
    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(permission), requestCode)
    }
}


