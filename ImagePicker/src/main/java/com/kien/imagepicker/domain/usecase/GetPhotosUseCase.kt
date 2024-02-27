package com.kien.imagepicker.domain.usecase

import com.kien.imagepicker.data.entity.Album
import com.kien.imagepicker.domain.repository.ImagePickerRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(val repo : ImagePickerRepository) {
    operator fun invoke(album: Album) = repo.getPhotos(album)
}