package com.kien.imagepicker.domain.usecase

import com.kien.imagepicker.domain.repository.ImagePickerRepository
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(val repo : ImagePickerRepository) {
    operator fun invoke() = repo.getAlbums()
}