package ru.practicum.android.diploma.domain.model

import ru.practicum.android.diploma.util.Constant.HTTP_NO_CONNECTIVITY
import ru.practicum.android.diploma.util.Constant.HTTP_UNKNOWN
import java.net.HttpURLConnection.HTTP_BAD_GATEWAY
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND

enum class Error(val code: Int) {
    NO_CONNECTIVITY(HTTP_NO_CONNECTIVITY),
    NOT_FOUND(HTTP_NOT_FOUND),
    UNKNOWN_ERROR(HTTP_UNKNOWN),
    BAD_REQUEST(HTTP_BAD_REQUEST),
    INTERNAL_SERVER_ERROR(HTTP_INTERNAL_ERROR),
    SERVICE_UNAVAILABLE(HTTP_BAD_GATEWAY),
    FORBIDDEN(HTTP_FORBIDDEN);

    companion object {
        fun getErrorMessage(exception: String): Error {
            var errorMessage = UNKNOWN_ERROR
            when (exception) {
                "HTTP 400 " -> errorMessage = BAD_REQUEST
                "HTTP 403 " -> errorMessage = FORBIDDEN
                "HTTP 404 " -> errorMessage = NOT_FOUND
                "HTTP 500 " -> errorMessage = INTERNAL_SERVER_ERROR
                "HTTP 503 " -> errorMessage = SERVICE_UNAVAILABLE
            }
            return errorMessage
        }
    }
}
