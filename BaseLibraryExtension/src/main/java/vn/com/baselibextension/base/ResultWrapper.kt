package vn.com.baselibextension.base

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val code: String, val message: String? = null) : ResultWrapper<Nothing>()
}