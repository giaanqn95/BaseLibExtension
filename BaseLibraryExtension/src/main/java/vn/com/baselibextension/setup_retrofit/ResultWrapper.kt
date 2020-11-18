package vn.com.baselibextension.setup_retrofit

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val code: String, val message: String? = null) : ResultWrapper<Nothing>()
}