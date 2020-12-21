package vn.com.baselibextension.setup_retrofit

/**
 * Created by giaan on 12/16/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class Request<T> {

    var end: () -> Unit = {}
    var loading: (isLoading: Boolean) -> Unit = {}
    var work: Work<T> = object : Work<T> {
        override fun onSuccess(result: ResultWrapper.Success<T>): ResultWrapper.Success<T> =
            result

        override fun onError(error: ResultWrapper.Error): ResultWrapper.Error = error
    }

    fun work(work: Work<T>) = apply {
        this.work = work
    }

    fun setEnd(end: () -> Unit) = apply {
        this.end = end
    }

    fun setLoading(loading: (isLoading: Boolean) -> Unit) = apply {
        this.loading = loading
    }

    inline fun work(
        crossinline onSuccess: (success: ResultWrapper.Success<T>) -> Unit = {},
        crossinline onError: (error: ResultWrapper.Error) -> Unit = {}
    ) = work(object : Work<T> {
        override fun onSuccess(result: ResultWrapper.Success<T>): ResultWrapper.Success<T> {
            onSuccess.invoke(result)
            return result
        }

        override fun onError(error: ResultWrapper.Error): ResultWrapper.Error {
            onError.invoke(error)
            return error
        }
    })

    inline fun end(crossinline onEnd: () -> Unit = {}) = setEnd {
        onEnd.invoke()
    }

    inline fun loading(crossinline onLoading: (isLoading: Boolean) -> Unit = {}) = setLoading {
        onLoading.invoke(it)
    }

    interface Work<T> {
        fun onSuccess(result: ResultWrapper.Success<T>): ResultWrapper.Success<T>

        fun onError(error: ResultWrapper.Error): ResultWrapper.Error
    }
}