package com.fourteen06.emseesquare.utils


sealed class Resource<T>(
    open val data: T? = null,
    open val message: String? = null
) {
    data class Loading<T>(override val data: T? = null) : Resource<T>(data = data)
    data class Success<T>(override val data: T) : Resource<T>(data = data)
    data class Error<T>(override val message: String) : Resource<T>(message = message)
}