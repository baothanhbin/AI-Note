package com.ainote.core.common.error

sealed class AppError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class DatabaseError(message: String, cause: Throwable? = null) : AppError(message, cause)
    class NetworkError(message: String, cause: Throwable? = null) : AppError(message, cause)
    class UnknownError(message: String, cause: Throwable? = null) : AppError(message, cause)
}
