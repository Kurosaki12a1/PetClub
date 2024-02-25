package com.kien.petclub.utils

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DataSnapshot
import io.mockk.every
import io.mockk.mockk

inline fun <reified T> mockTask(exception: Exception? = null): Task<T> {
    val task: Task<T> = mockk(relaxed = true)
    every { task.isComplete } returns true
    every { task.exception } returns exception
    every { task.isCanceled } returns false
    val relaxedVoid1: Task<T> = mockk(relaxed = true)
    every { task.addOnCompleteListener { } } returns relaxedVoid1
    val relaxedVoid: T = mockk(relaxed = true)
    every { task.result } returns relaxedVoid
    return task
}

inline fun <reified T> mockDataSnapShot(
    snapshot: DataSnapshot,
    converter: (DataSnapshot) -> T
): List<T> {
    return snapshot.children.mapNotNull {
        converter(it)
    }
}

fun DataSnapshot.mockDataSnapshotChildString(childName: String) {
    val childSnapshot = mockk<DataSnapshot>(relaxed = true)
    every { child(childName) } returns childSnapshot
    every { childSnapshot.getValue(String::class.java) } returns childName
}

fun DataSnapshot.mockDataSnapshotChildLong(childName: String) {
    every { child(childName).getValue(Long::class.java) } returns 1L
}

fun DataSnapshot.mockDataSnapshotChildForList(
    childName: String
) {
    val childSnapshot = mockk<DataSnapshot>(relaxed = true)
    val iterableSnapShot = mockk<Iterable<DataSnapshot>>(relaxed = true)
    every { child(childName).children } returns listOf(childSnapshot)
    every { listOf(childSnapshot).map { it.getValue(String::class.java) }} returns listOf(childName)
}

inline fun <reified T> Task<T>.awaitTask(): Task<T> {
    val taskCompletionSource: TaskCompletionSource<T> = mockk()
    taskCompletionSource.setResult(result)
    exception?.let { taskCompletionSource.setException(it) } ?: run {
        taskCompletionSource.setResult(null)
    }
    return taskCompletionSource.task
}
