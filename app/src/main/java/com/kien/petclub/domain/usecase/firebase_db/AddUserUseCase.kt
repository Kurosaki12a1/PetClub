package com.kien.petclub.domain.usecase.firebase_db

import android.util.Log
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddUserUseCase @Inject constructor(private val repo: FirebaseDBRepository) {
    operator fun invoke(uid: String, name: String, phone: String): Flow<Resource<Unit>> {
        if (uid.isEmpty()) {
            return flow { emit(Resource.Failure(IllegalArgumentException("id is empty"))) }
        }
        Log.e("AddUserUseCase", "invoke: $uid $name $phone")
        return repo.addUserDatabase(uid, name, phone)
    }
}