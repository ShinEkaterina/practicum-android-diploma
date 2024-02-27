package ru.practicum.android.diploma.domain.model

enum class ErrorMessage {
    NO_CONNECTIVITY_MESSAGE,
    SERVER_ERROR_MESSAGE,
    NOT_FOUND,
    UNKNOWN_ERROR,
    BAD_REQUEST,
    INTERNAL_SERVER_ERROR,
    SERVICE_UNAVAILABLE,
    FORBIDDEN;

    companion object {
        fun getErrorMessage(exception: String): ErrorMessage {
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
