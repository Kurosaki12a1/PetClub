package com.kien.petclub.data.repository

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kien.petclub.data.data_source.local.AppDatabase
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.repository.FilterProductRepository
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.utils.readJsonFromAssets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FilterProductRepositoryImpl @Inject constructor(
    val context: Context,
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FilterProductRepository {
    override fun add(filterProduct: FilterProductEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            database.filterDao.insert(filterProduct)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun add(filterProducts: List<FilterProductEntity>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            filterProducts.forEach {
                database.filterDao.insert(it)
            }
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)
    override fun update(filterProduct: FilterProductEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            database.filterDao.update(filterProduct)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun delete(filterProduct: FilterProductEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            database.filterDao.delete(filterProduct)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun delete(id: Long): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            database.filterDao.delete(id)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)


    override fun loadAll(): Flow<Resource<List<FilterProductEntity>>> = flow {
        emit(Resource.Loading)
        try {
            val listFilter = database.filterDao.loadAll()
            emit(Resource.Success(listFilter))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)


    override fun getFilterProductById(id: Long): Flow<Resource<FilterProductEntity?>> = flow {
        emit(Resource.Loading)
        try {
            val filterProduct = database.filterDao.getFilterProductById(id)
            emit(Resource.success(filterProduct))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }.flowOn(dispatcher)


    override fun getFilterProductByName(name: String): Flow<Resource<FilterProductEntity?>> = flow {
        emit(Resource.Loading)
        try {
            val filterProduct = database.filterDao.getFilterProductByName(name)
            emit(Resource.success(filterProduct))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }.flowOn(dispatcher)

    override fun getFromAssets(): Flow<Resource<List<FilterProductEntity>?>> = flow {
        emit(Resource.Loading)
        try {
            val jsonString = readJsonFromAssets(context, "data_filter.json")
            val gson = Gson()
            val listType = object : TypeToken<List<FilterProductEntity>>() {}.type
            val result = gson.fromJson<List<FilterProductEntity>>(jsonString, listType)
            emit(Resource.success(result))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }.flowOn(dispatcher)

}

