package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.model.entity.User
import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val repo: FirebaseDBRepository) {
    operator fun invoke(userId: String, user: User) = repo.updateUserDatabase(userId, user)
}