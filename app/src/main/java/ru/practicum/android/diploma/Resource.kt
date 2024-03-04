package ru.practicum.android.diploma

import ru.practicum.android.diploma.domain.model.NetworkError

sealed class Resource<T>(
    val data: T? = null,
    val message: NetworkError? = null
) {

    class Success<T>(
        data: T
    ) : Resource<T>(data)

    class Error<T>(
        message: NetworkError,
        data: T? = null
    ) : Resource<T>(data, message)

}
