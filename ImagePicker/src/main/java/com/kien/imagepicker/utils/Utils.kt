package com.kien.imagepicker.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.kien.imagepicker.R
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun createImageUri(contentResolver: ContentResolver): Uri {
    val timeStamp = convertMillisToDate()
    val imageFileName = "PET_CLUB_$timeStamp.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
}

private fun convertMillisToDate(): String {
    val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val calender = Calendar.getInstance()
    calender.timeInMillis = System.currentTimeMillis()
    return format.format(calender.time)
}

fun Activity.initTransitionClose() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        overrideActivityTransition(
            Activity.OVERRIDE_TRANSITION_CLOSE,
            R.anim.anim_slide_in_left,
            R.anim.anim_slide_out_right
        )
    } else {
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
    }
    finish()
}

fun saveUriListsToFile(context: Context, images: ArrayList<Uri>): String {
    val fileName = "temp_pet_club"
    val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
    ObjectOutputStream(fos).use { it.writeObject(images.map { str -> str.toString() }) }
    fos.close()
    return fileName
}

fun readUriListFromFile(context: Context): ArrayList<Uri> {
    val fis: FileInputStream = context.openFileInput("temp_pet_club")
    var uriList: ArrayList<String> = ArrayList()
    try {
        ObjectInputStream(fis).use { ois -> uriList = ois.readObject() as ArrayList<String> }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    fis.close()

    // Đảm bảo xóa file sau khi sử dụng để không chiếm dụng bộ nhớ không cần thiết
    context.deleteFile("temp_pet_club")
    return uriList.map { Uri.parse(it) } as ArrayList<Uri>
}

fun RecyclerView.slideUpAnimation() {
    val slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_in)
    slideUpAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            visibility = View.VISIBLE
        }

        override fun onAnimationEnd(p0: Animation?) {
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }

    })
    startAnimation(slideUpAnimation)
}

fun RecyclerView.slideDownAnimation() {
    val slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_out)
    slideDownAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {}

        override fun onAnimationEnd(p0: Animation?) {
             visibility = View.GONE
        }

        override fun onAnimationRepeat(p0: Animation?) {}
    })
   startAnimation(slideDownAnimation)
}

fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun View.getVisibleRect(): Rect {
    val outRect = Rect()
    getGlobalVisibleRect(outRect)
    return outRect
}

fun View.isInVisibleRect(x: Int, y: Int): Boolean {
    val outRect = getVisibleRect()
    return outRect.contains(x, y)
}