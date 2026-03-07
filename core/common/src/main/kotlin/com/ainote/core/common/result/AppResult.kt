package com.ainote.core.common.result

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val error: com.ainote.core.common.error.AppError) : AppResult<Nothing>
    object Loading : AppResult<Nothing>
}
