package com.kien.petclub.domain.util

sealed class Resource<out T> {

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)

        fun failure(
            error: Throwable,
            errorMessage : String? = null,
        ): Resource<Nothing> = Failure(error, errorMessage)
    }

    class Success<out T>(val value: T) : Resource<T>()

    class Failure(
        error: Throwable,
        errorMessage : String? = null,
    ) : Resource<Nothing>()
}

enum class FailureStatus {
    EMPTY,
    API_FAIL,
    NO_INTERNET,
    OTHER
}