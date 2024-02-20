package com.kien.petclub.domain.usecase.storage

class ImageUseCase(
    val uploadImageUseCase: UploadImageUseCase,
    val downloadImageUseCase: DownloadImageUseCase
)