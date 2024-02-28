package com.kien.petclub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.constants.Constants
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.mapSnapshotToService
import com.kien.petclub.domain.repository.ServiceRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    auth: FirebaseAuth,
    db: FirebaseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ServiceRepository {
    private val serviceDatabase = db.reference.child(Constants.SERVICE_DB)
    private val userId = auth.currentUser?.uid ?: ""

    override fun getServiceDatabase(): Flow<Resource<ArrayList<Product.Service>>?> = flow {
        emit(Resource.Loading)
        val snapshot = serviceDatabase.child(userId).get().await()
        try {
            val listService = snapshot.children.mapNotNull { mapSnapshotToService(it) }
            emit(Resource.success(ArrayList(listService)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun getServiceById(id: String): Flow<Resource<Product.Service?>> = flow {
        emit(Resource.Loading)
        val snapshot = serviceDatabase.child(userId).child(id).get().await()
        try {
            emit(Resource.success(mapSnapshotToService(snapshot)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }

    override fun addServiceDatabase(service: Product.Service): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val ref = serviceDatabase.child(userId)
            ref.child(service.id).setValue(service).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun updateServiceDatabase(
        serviceId: String,
        data: Product.Service
    ): Flow<Resource<Product>> =
        flow {
            emit(Resource.Loading)
            try {
                serviceDatabase.child(userId).child(serviceId).setValue(data).await()
                emit(Resource.success(data))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(dispatcher)

    override fun deleteServiceDatabase(serviceId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            serviceDatabase.child(userId).child(serviceId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun checkServiceExist(id: String): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            try {
                val snapshot = serviceDatabase.child(id).get().await()
                emit(Resource.success(snapshot.exists()))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(dispatcher)
}