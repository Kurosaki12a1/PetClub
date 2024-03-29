package com.kien.petclub.domain.usecase.firebase_db.user

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val repo : FirebaseDBRepository){
    operator fun invoke(uid: String) = repo.deleteUserDatabase(uid)
}