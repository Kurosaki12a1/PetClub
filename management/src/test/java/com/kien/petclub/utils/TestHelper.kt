package com.kien.petclub.utils

import com.google.android.gms.tasks.Task
import io.mockk.every
import io.mockk.mockk
import java.lang.Exception

inline fun <reified T> mockTask(exception: Exception? = null): Task<T> {
    val task: Task<T> = mockk(relaxed = true)
    every { task.isComplete } returns true
    every { task.exception } returns exception
    every { task.isCanceled } returns false
    val relaxedVoid: T = mockk(relaxed = true)
    every { task.result } returns relaxedVoid
    return task
}