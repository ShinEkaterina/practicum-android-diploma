package ru.practicum.android.diploma.data.dto.respone

open class Response {
    var responseCode = NO_RESULT_RESULT_CODE

    companion object {
        const val NO_RESULT_RESULT_CODE = 0
        const val NO_INTERNET_RESULT_CODE = -1
        const val UNKNOWN_RESULT_RESULT_CODE = -2
        const val SERVER_ERROR_RESULT_CODE = 500
        const val BAD_REQUEST_RESULT_CODE = 400
        const val SUCCESS_RESULT_CODE = 200
        const val NOT_FOUND_RESULT_CODE = 404
        const val CAPTCHA_INPUT_RESULT_CODE = 403
    }
}
