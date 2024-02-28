package com.kien.petclub.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.constants.Constants
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.mapSnapshotToInfoProduct
import com.kien.petclub.domain.repository.InfoProductRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InfoProductRepositoryImpl @Inject constructor(
    db: FirebaseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : InfoProductRepository {
    private val typeProductDatabase = db.reference.child(Constants.TYPE_DB)
    private val brandProductDatabase = db.reference.child(Constants.BRAND_DB)
    private val locationProductDatabase = db.reference.child(Constants.LOCATION_DB)

    override fun getTypeProducts(): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        val snapshot = typeProductDatabase.get().await()
        try {
            val listType = snapshot.children.mapNotNull { mapSnapshotToInfoProduct(it) }
            emit(Resource.success(ArrayList(listType)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

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
    }.flowOn(dispatcher)


    override fun getBrandProducts(): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        val snapshot = brandProductDatabase.get().await()
        try {
            val listType = snapshot.children.mapNotNull { it.getValue(InfoProduct::class.java) }
            emit(Resource.success(ArrayList(listType)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

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
    }.flowOn(dispatcher)

    override fun getLocationProducts(): Flow<Resource<ArrayList<InfoProduct>>> = flow {
        emit(Resource.Loading)
        val snapshot = locationProductDatabase.get().await()
        try {
            val listLocation = snapshot.children.mapNotNull { it.getValue(InfoProduct::class.java) }
            emit(Resource.success(ArrayList(listLocation)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

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
    }.flowOn(dispatcher)

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
    }.flowOn(dispatcher)


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
    }.flowOn(dispatcher)

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
    }.flowOn(dispatcher)

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
    }.flowOn(dispatcher)


    override fun deleteBrandProduct(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            brandProductDatabase.child(id).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun deleteLocationProduct(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            locationProductDatabase.child(id).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)
}