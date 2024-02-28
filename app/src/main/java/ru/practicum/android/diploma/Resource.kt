package ru.practicum.android.diploma

sealed class Resource<T>(
    val data: T? = null,
    val message: ru.practicum.android.diploma.domain.model.Error? = null
) {

    class Success<T>(
        data: T
    ) : Resource<T>(data)

    class Error<T>(
        message: ru.practicum.android.diploma.domain.model.Error,
        data: T? = null
    ) : Resource<T>(data, message)

}
