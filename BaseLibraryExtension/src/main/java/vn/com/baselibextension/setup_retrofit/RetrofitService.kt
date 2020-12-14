package vn.com.baselibextension.setup_retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import vn.com.baselibextension.dj.module.ApiClientModule
import vn.com.baselibextension.utils.Constants


/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 10:36 AM
 */

class RetrofitService<T>(val context: Context, val value: T) {

    private var apiInterface: ApiInterface = ApiClientModule.providePostApi()

    private var end: () -> Unit = {}
    private var loading: (isLoading: Boolean) -> Unit = {}
    private var work: Work<T> = object : Work<T> {
        override fun onSuccess(result: ResultWrapper.Success<T>): ResultWrapper.Success<T> =
            result

        override fun onError(error: ResultWrapper.Error): ResultWrapper.Error = error
    }
    private var processResponse: Process<T>? = null

    private lateinit var apiCall: (suspend () -> T)
    private var listResult: MutableList<ResultWrapper<T>> = ArrayList()
    private var codeRequired: Any = Any()

    private suspend fun getMethod(
        headers: Map<String, String>,
        url: String,
        message: Any? = null,
        codeRequired: Any
    ) {
        this.apiCall = { apiInterface.get(headers, url + message) }
        this.codeRequired = codeRequired
    }

    private suspend fun postMethod(
        headers: Map<String, String>,
        url: String,
        message: Any? = null,
        codeRequired: Any
    ) = apply {
        this.apiCall = { apiInterface.post(headers, url, message) }
        this.codeRequired = codeRequired
        return this
    }

    private suspend fun putMethod(
        headers: Map<String, String>,
        url: String,
        message: Any? = null,
        codeRequired: Any
    ) {
        this.apiCall = { apiInterface.put(headers, url, message) }
        this.codeRequired = codeRequired
    }

    private suspend fun deleteMethod(
        headers: Map<String, String>,
        url: String,
        message: Any?,
        codeRequired: Any
    ) {
        this.apiCall = { apiInterface.delete(headers, url, message) }
        this.codeRequired = codeRequired
    }

    suspend fun request(repo: Repo) = apply {
        when (repo.typeRepo) {
            TypeRepo.GET -> {
                getMethod(repo.headers, repo.url, repo.message, repo.codeRequired)
            }
            TypeRepo.POST -> {
                postMethod(repo.headers, repo.url, repo.message, repo.codeRequired)
            }
            TypeRepo.PUT -> {
                putMethod(repo.headers, repo.url, repo.message, repo.codeRequired)
            }
            TypeRepo.DELETE -> {
                deleteMethod(repo.headers, repo.url, repo.message, repo.codeRequired)
            }
        }
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

    fun setProcessResponse(process: Process<T>) = apply {
        this.processResponse = process
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

    fun merge(vararg build: ResultWrapper<T>) = apply {
        listResult = build.toMutableList()
        return this
    }

    fun merge(build: MutableList<ResultWrapper<T>>) = apply {
        listResult = build
        return this
    }

    suspend fun build(): ResultWrapper<T> {
        loading.invoke(true)
        return safeApiCall2(apiCall, codeRequired).apply {
            end.invoke()
            loading.invoke(false)
        }
    }

    suspend fun buildMerge() {
        loading.invoke(true)
        listResult.forEach {
            if (it !is ResultWrapper.Success) {
                work.onError(ResultWrapper.Error((it as ResultWrapper.Error).code))
                end.invoke()
                loading.invoke(false)
                return
            }
        }
        loading.invoke(false)
        work.onSuccess(ResultWrapper.Success(value))
    }

    private fun isOnline(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

    private suspend fun safeApiCall2(apiCall: suspend () -> T, codeRequired: Any): ResultWrapper<T> {
        return withTimeout(Constants.TIME_OUT) {
            if (!isOnline())
                return@withTimeout work.onError(ResultWrapper.Error(ErrorType.NO_INTERNET.code))
            try {
                val response = apiCall.invoke()
                val process = processResponse!!.process(JSON.encode(response), codeRequired)
                if (process is ResultWrapper.Success<T>) {
                    work.onSuccess(ResultWrapper.Success(process.value))
                } else {
                    work.onError(ResultWrapper.Error((process as ResultWrapper.Error).code, process.message))
                }
                return@withTimeout process
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        val code = throwable.code()
                        if (code in 500..599)
                            return@withTimeout work.onError(
                                ResultWrapper.Error(
                                    ErrorType.ERROR_FORM_SERVER.code,
                                    throwable.message
                                )
                            )
                        else if (code in 400..499)
                            return@withTimeout work.onError(
                                ResultWrapper.Error(
                                    ErrorType.ERROR_FORM_CLIENT.code,
                                    throwable.message
                                )
                            )

                        return@withTimeout work.onError(
                            ResultWrapper.Error(
                                ErrorType.UNKNOW_ERROR_FROM_SERVER.code,
                                throwable.message
                            )
                        )
                    }
                    else -> return@withTimeout work.onError(
                        ResultWrapper.Error(
                            ErrorType.UNKNOW_ERROR_FROM_SERVER.code,
                            throwable.message
                        )
                    )
                }
            }
        }
    }

    interface Work<T> {
        fun onSuccess(result: ResultWrapper.Success<T>): ResultWrapper.Success<T>

        fun onError(error: ResultWrapper.Error): ResultWrapper.Error
    }

    interface Process<T> {
        fun process(response: String, codeRequire: Any): ResultWrapper<T>
    }
}