package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repo : FirebaseDBRepository){
    operator fun invoke(uid: String) = repo.getUserDatabase(uid)
}