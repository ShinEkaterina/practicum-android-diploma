package ru.practicum.android.diploma

import ru.practicum.android.diploma.domain.model.ErrorMessage

sealed class Resource<T>(val data: T? = null, val message: ErrorMessage? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: ErrorMessage, data: T? = null) : Resource<T>(data, message)
}
