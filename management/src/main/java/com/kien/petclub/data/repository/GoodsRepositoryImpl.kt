package com.kien.petclub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.constants.Constants
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.mapSnapshotToGoods
import com.kien.petclub.domain.repository.GoodsRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoodsRepositoryImpl @Inject constructor(
    auth: FirebaseAuth,
    db: FirebaseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GoodsRepository {

    private val goodsDatabase = db.reference.child(Constants.GOODS_DB)
    private val userId = auth.currentUser?.uid ?: ""

    override fun getGoodsDatabase(): Flow<Resource<ArrayList<Product.Goods>>> = flow {
        emit(Resource.Loading)
        val reference = goodsDatabase.child(userId)
        val snapshot = reference.get().await()
        try {
            val listGoods = snapshot.children.mapNotNull { mapSnapshotToGoods(it) }
            emit(Resource.success(ArrayList(listGoods)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)


    override fun getGoodsById(id: String): Flow<Resource<Product.Goods?>> = flow {
        emit(Resource.Loading)
        val snapshot = goodsDatabase.child(userId).child(id).get().await()
        try {
            emit(Resource.success(mapSnapshotToGoods(snapshot)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }

    override fun addGoodsDatabase(goods: Product.Goods): Flow<Resource<Unit>> = flow {
        try {
            val ref = goodsDatabase.child(userId)
            ref.child(goods.id).setValue(goods).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun updateGoodsDatabase(
        goodsId: String,
        data: Product.Goods
    ): Flow<Resource<Product>> =
        flow {
            emit(Resource.Loading)
            try {
                goodsDatabase.child(userId).child(goodsId).setValue(data).await()
                emit(Resource.success(data))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(dispatcher)

    override fun deleteGoodsDatabase(goodsId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            goodsDatabase.child(userId).child(goodsId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun checkGoodsExist(id: String): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            try {
                val snapshot = goodsDatabase.child(id).get().await()
                emit(Resource.success(snapshot.exists()))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(dispatcher)

}