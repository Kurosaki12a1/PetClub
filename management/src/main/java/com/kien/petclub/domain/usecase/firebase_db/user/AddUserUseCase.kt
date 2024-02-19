package com.kien.petclub.domain.usecase.firebase_db.user

import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddUserUseCase @Inject constructor(private val repo: FirebaseDBRepository) {
    operator fun invoke(uid: String, email: String, name: String, phone: String): Flow<Resource<Unit>> {
        if (uid.isEmpty()) {
            return flow { emit(Resource.Failure(IllegalArgumentException("id is empty"))) }
        }
        flow { emit(Resource.Loading) }
        return repo.addUserDatabase(uid, email, name, phone)
    }
}