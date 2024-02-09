package com.kien.petclub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.constants.Constants
import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.model.entity.User
import com.kien.petclub.domain.model.entity.mapSnapshotToInfoProduct
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
    private val typeProductDatabase = db.reference.child(Constants.TYPE_DB)
    private val brandProductDatabase = db.reference.child(Constants.BRAND_DB)
    private val locationProductDatabase = db.reference.child(Constants.LOCATION_DB)

    //   private val notificationDatabase = db.reference.child(Constants.NOTIFICATION_DB)
    private val userDatabase = db.reference.child(Constants.USER_DB)

    private val userId = auth.currentUser?.uid ?: ""
    override fun getGoodsDatabase(): Flow<Resource<ArrayList<Goods>>?> = flow {
        emit(Resource.Loading)
        val reference = goodsDatabase.child(userId)
        val snapshot = reference.get().await()
        try {
            val listGoods = snapshot.children.mapNotNull { it.getValue(Goods::class.java) }
            emit(Resource.success(ArrayList(listGoods)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getServiceDatabase(): Flow<Resource<ArrayList<Service>>?> = flow {
        emit(Resource.Loading)
        val snapshot = serviceDatabase.child(userId).get().await()
        try {
            val listService = snapshot.children.mapNotNull { it.getValue(Service::class.java) }
            emit(Resource.success(ArrayList(listService)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getGoodsById(id: String): Flow<Resource<Goods?>> = flow {
        emit(Resource.Loading)
        val snapshot = goodsDatabase.child(userId).child(id).get().await()
        try {
            emit(Resource.success(snapshot.getValue(Goods::class.java)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }

    override fun getServiceById(id: String): Flow<Resource<Service?>> = flow {
        emit(Resource.Loading)
        val snapshot = serviceDatabase.child(userId).child(id).get().await()
        try {
            emit(Resource.success(snapshot.getValue(Service::class.java)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }

    override fun addUserDatabase(
        userId: String,
        email: String,
        name: String,
        phoneNumber: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val user = User(userId, email, name, phoneNumber)
            userDatabase.child(userId).setValue(user).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateUserDatabase(userId: String, user: User): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            userDatabase.child(userId).setValue(user).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserDatabase(userId: String): Flow<Resource<User?>> = flow {
        emit(Resource.Loading)
        val snapshot = userDatabase.child(userId).get().await()
        try {
            emit(Resource.success(snapshot.getValue(User::class.java)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteUserDatabase(userId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            userDatabase.child(userId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

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
        emit(Resource.Loading)
        try {
            val ref = serviceDatabase.child(userId)
            ref.child(service.id).setValue(service).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateGoodsDatabase(goodsId: String, data: Goods): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            goodsDatabase.child(userId).child(goodsId).setValue(data).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateServiceDatabase(serviceId: String, data: Service): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            try {
                serviceDatabase.child(userId).child(serviceId).setValue(data).await()
                emit(Resource.success(Unit))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun deleteGoodsDatabase(goodsId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            goodsDatabase.child(userId).child(goodsId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteServiceDatabase(serviceId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            serviceDatabase.child(userId).child(serviceId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun checkGoodsExist(id: String): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            try {
                val snapshot = goodsDatabase.child(id).get().await()
                emit(Resource.success(snapshot.exists()))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun checkServiceExist(id: String): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            try {
                val snapshot = serviceDatabase.child(id).get().await()
                emit(Resource.success(snapshot.exists()))
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun getTypeProducts(): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        val snapshot = typeProductDatabase.get().await()
        try {
            val listType = snapshot.children.mapNotNull { mapSnapshotToInfoProduct(it) }
            emit(Resource.success(ArrayList(listType)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addTypeProduct(type: InfoProduct): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            // Add parent type
            if (type.parentId.isNullOrBlank()) {
                // Generate parent key id because type.id is empty mean no child
                val key =
                    typeProductDatabase.push().key ?: throw Exception("Failed to generate key")
                typeProductDatabase.child(key).setValue(type.also { it.id = key }).await()
            } else {
                // Add child type to parent with type.id is parent id
                val ref = typeProductDatabase.child(type.parentId!!).child("child")
                // Generate child id
                val key = ref.push().key ?: throw Exception("Failed to generate key")
                // Set value child to database
                ref.child(key).setValue(type.also {
                    it.id = key
                    it.parentId = type.parentId
                }).await()
            }
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)


    override fun getBrandProducts(): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        val snapshot = brandProductDatabase.get().await()
        try {
            val listType = snapshot.children.mapNotNull { it.getValue(InfoProduct::class.java) }
            emit(Resource.success(ArrayList(listType)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addBrandProduct(name: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val key =
                brandProductDatabase.push().key ?: throw Exception("Failed to generate key")
            brandProductDatabase.child(key).setValue(InfoProduct(key, name)).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getLocationProducts(): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        val snapshot = locationProductDatabase.get().await()
        try {
            val listLocation = snapshot.children.mapNotNull { it.getValue(InfoProduct::class.java) }
            emit(Resource.success(ArrayList(listLocation)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addLocationProduct(name: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val key =
                locationProductDatabase.push().key ?: throw Exception("Failed to generate key")
            locationProductDatabase.child(key).setValue(InfoProduct(key, name)).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun searchForBrands(name: String): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        // Query by key, startAt by name and endAt by name + "\uf8ff" to get all the brand that start with name
        val snapshot =
            brandProductDatabase.orderByChild("name").startAt(name).endAt(name + "\uf8ff").get()
                .await()
        try {
            val listBrand = snapshot.children.mapNotNull { it.getValue(InfoProduct::class.java) }
            emit(Resource.success(ArrayList(listBrand)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)


    override fun searchForTypes(name: String): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        val snapShot = typeProductDatabase.get().await()
        try {
            val listTotalTypeProduct = snapShot.children.mapNotNull { mapSnapshotToInfoProduct(it) }
            val result = HashMap<String, InfoProduct>()
            listTotalTypeProduct.forEach { parent ->
                if (parent.name.lowercase().startsWith(name.lowercase())) {
                    result[parent.id] = parent
                }
                parent.child?.forEach { child ->
                    if (child.name.lowercase().startsWith(name.lowercase())) {
                        result[parent.id] = parent
                    }
                }
            }
            emit(Resource.success(result.values.toMutableList() as ArrayList<InfoProduct>))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun searchForLocations(name: String): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        // Query by key, startAt by name and endAt by name + "\uf8ff" to get all the location that start with name
        val snapshot =
            locationProductDatabase.orderByChild("name").startAt(name).endAt(name + "\uf8ff").get()
                .await()
        try {
            val listLocation = snapshot.children.mapNotNull { it.getValue(InfoProduct::class.java) }
            emit(Resource.success(ArrayList(listLocation)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteTypeProduct(id: String, parentId: String?): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            if (parentId != null) {
                typeProductDatabase.child(parentId).child("child").child(id).removeValue().await()
            } else {
                typeProductDatabase.child(id).removeValue().await()
            }
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)


    override fun deleteBrandProduct(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            brandProductDatabase.child(id).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteLocationProduct(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            locationProductDatabase.child(id).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}