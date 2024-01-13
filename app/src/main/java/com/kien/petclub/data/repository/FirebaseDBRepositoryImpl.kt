package com.kien.petclub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kien.petclub.data.constants.Constants
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.model.entity.Service
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseDBRepositoryImpl @Inject constructor(
    auth: FirebaseAuth,
    db: FirebaseDatabase
) : FirebaseDBRepository {
    private val goodsDatabase = db.reference.child(Constants.GOODS_DB)
    private val serviceDatabase = db.reference.child(Constants.SERVICE_DB)
    private val notificationDatabase = db.reference.child(Constants.NOTIFICATION_DB)

    private val userId = auth.currentUser?.uid ?: ""
    override fun getGoodsDatabase(userId: String): Flow<ArrayList<Goods>> = callbackFlow {
        val listener = object : ValueEventListener {
            val listGoods = ArrayList<Goods>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    listGoods.add(data.getValue(Goods::class.java)!!)
                }
                trySend(listGoods)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        goodsDatabase.child(userId).addValueEventListener(listener)
        awaitClose { goodsDatabase.removeEventListener(listener) }
    }

    override fun getServiceDatabase(userId: String): Flow<ArrayList<Service>> = callbackFlow {
        val listener = object : ValueEventListener {
            val listService = ArrayList<Service>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    listService.add(data.getValue(Service::class.java)!!)
                }
                trySend(listService)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        goodsDatabase.child(userId).addValueEventListener(listener)
        awaitClose { goodsDatabase.removeEventListener(listener) }
    }

    override fun addGoodsDatabase(goods: Goods): Flow<Boolean> = callbackFlow {
        goodsDatabase.child(userId).setValue(goods).addOnCompleteListener {
            trySend(it.isSuccessful)
        }
    }

    override fun addServiceDatabase(service: Service): Flow<Boolean> = callbackFlow {
        serviceDatabase.child(userId).setValue(service).addOnCompleteListener {
            trySend(it.isSuccessful)
        }
    }


    override fun updateGoodsDatabase(goodsId: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun updateServiceDatabase(serviceId: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun deleteGoodsDatabase(goodsId: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun deleteServiceDatabase(serviceId: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}