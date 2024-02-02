package com.kien.petclub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.constants.Constants
import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDBRepositoryImpl @Inject constructor(
    auth: FirebaseAuth,
    db: FirebaseDatabase
) : FirebaseDBRepository {
    private val goodsDatabase = db.reference.child(Constants.GOODS_DB)
    private val serviceDatabase = db.reference.child(Constants.SERVICE_DB)
    private val notificationDatabase = db.reference.child(Constants.NOTIFICATION_DB)

    private val userId = auth.currentUser?.uid ?: ""
    override fun getGoodsDatabase(): Flow<ArrayList<Goods>?> = flow {
        val reference = goodsDatabase.child(userId)
        val snapshot = reference.get().await()
        try {
            val listGoods = snapshot.children.mapNotNull { it.getValue(Goods::class.java) }
            emit(ArrayList(listGoods))
        } catch (e: Exception) {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    override fun getServiceDatabase(): Flow<ArrayList<Service>?> = flow {
        val snapshot = serviceDatabase.child(userId).get().await()
        try {
            val listService = snapshot.children.mapNotNull { it.getValue(Service::class.java) }
            emit(ArrayList(listService))
        } catch (e: Exception) {
            // NO DATA
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    override fun getGoodsById(id: String): Flow<Goods?> = flow {
        val snapshot = goodsDatabase.child(userId).child(id).get().await()
        try {
            emit(snapshot.getValue(Goods::class.java))
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun getServiceById(id: String): Flow<Service?> = flow {
        val snapshot = serviceDatabase.child(userId).child(id).get().await()
        try {
            emit(snapshot.getValue(Service::class.java))
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun addGoodsDatabase(goods: Goods): Flow<Resource<Unit>> = flow {
        try {
            val ref = goodsDatabase.child(userId)
            ref.child(goods.id).setValue(goods).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addServiceDatabase(service: Service): Flow<Resource<Unit>> = flow {
        try {
            val ref = serviceDatabase.child(userId)
            ref.child(service.id).setValue(service).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }

    override fun updateGoodsDatabase(goodsId: String, data: Goods): Flow<Resource<Unit>> = flow {
        try {
            goodsDatabase.child(userId).child(goodsId).setValue(data).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }

    override fun updateServiceDatabase(serviceId: String, data: Service): Flow<Resource<Unit>> =
        flow {
            try {
                serviceDatabase.child(userId).child(serviceId).setValue(data).await()
                emit(Resource.success(Unit))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }

    override fun deleteGoodsDatabase(goodsId: String): Flow<Resource<Unit>> = flow {
        try {
            goodsDatabase.child(userId).child(goodsId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }

    override fun deleteServiceDatabase(serviceId: String): Flow<Resource<Unit>> = flow {
        try {
            serviceDatabase.child(userId).child(serviceId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }
}