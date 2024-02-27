package com.kien.petclub.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.utils.goodsList
import com.kien.petclub.utils.mockDataSnapshotListWithGoods
import com.kien.petclub.utils.mockTask
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DatabaseRepositoryTest {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var repo: FirebaseDBRepositoryImpl
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        auth = mockk(relaxed = true)
        database = mockk(relaxed = true)
        ref = mockk(relaxed = true)

        coEvery { database.reference } returns ref

        repo = FirebaseDBRepositoryImpl(auth, database, testDispatcher)
    }

    @Test
    fun `getGoodsDatabase emit success`(): Unit = runTest(testDispatcher) {
        coEvery { any<Task<DataSnapshot>>().await() } returns mockDataSnapshotListWithGoods()

        val taskSnapshot = mockTask<DataSnapshot>()
        coEvery { ref.child(any()) } returns ref
        coEvery { ref.get() } returns taskSnapshot
        coEvery { taskSnapshot.result } returns mockDataSnapshotListWithGoods()
        val result = repo.getGoodsDatabase().toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Success && (result.last() as Resource.Success).value.size == goodsList.size)
    }

    @Test
    fun `getGoodsDatabase emit failure`(): Unit = runTest(testDispatcher) {
        val exceptions = mockk<Exception>(relaxed = true)
//        coEvery { any<Task<DataSnapshot>>().isComplete} returns true
//        coEvery { any<Task<DataSnapshot>>().isCanceled} returns false
//        coEvery { any<Task<DataSnapshot>>().exception} returns exceptions
        //     coEvery { any<Task<DataSnapshot>>().await() } throws exceptions

        val taskSnapshot = mockTask<DataSnapshot>(Exception())
        coEvery { ref.child(any()) } returns ref
        coEvery { ref.get() } returns taskSnapshot
        coEvery { taskSnapshot.await() } throws Exception()
        val result = repo.getGoodsDatabase().toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure)
    }
}

