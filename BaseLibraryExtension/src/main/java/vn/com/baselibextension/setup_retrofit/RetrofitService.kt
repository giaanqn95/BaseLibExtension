package vn.com.baselibextension.setup_retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import vn.com.baselibextension.InjectContext
import vn.com.baselibextension.dj.component.DaggerApiClientComponent
import vn.com.baselibextension.dj.module.ApiClientModule
import vn.com.baselibextension.utils.Constants
import javax.inject.Inject


/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 10:36 AM
 */

class RetrofitService {

    @Inject
    lateinit var apiInterface: ApiInterface
    private var work: Work = object : Work {
        override fun onSuccess(result: ResultWrapper.Success<BaseResponse>): ResultWrapper.Success<BaseResponse> =
            result

        override fun onError(error: ResultWrapper.Error): ResultWrapper.Error = error
    }
    private var end: () -> Unit = {}
    private var loading: (isLoading: Boolean) -> Unit = {}
    private var apiCall: (suspend () -> BaseResponse) = { BaseResponse() }
    private var codeRequired: Any = Any()
    private var listResult: MutableList<ResultWrapper<BaseResponse>> = ArrayList()

    private suspend fun getMethod(
        headers: Map<String, String>,
        url: String,
        message: Any? = null,
        codeRequired: Any
    ) {
        this.apiCall = { apiInterface.get(headers, url) }
        this.codeRequired = codeRequired
    }

    private suspend fun postMethod(
        headers: Map<String, String>,
        url: String,
        message: Any? = null,
        codeRequired: Any
    ): RetrofitService {
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

    suspend fun request(repo: Repo): RetrofitService {
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
        return this
    }

    fun work(work: Work) = apply {
        this.work = work
    }

    fun setEnd(end: () -> Unit) = apply {
        this.end = end
    }

    fun setLoading(loading: (isLoading: Boolean) -> Unit) = apply {
        this.loading = loading
    }

    inline fun work(
        crossinline onSuccess: (success: ResultWrapper.Success<BaseResponse>) -> Unit = {},
        crossinline onError: (error: ResultWrapper.Error) -> Unit = {}
    ) = work(object : Work {
        override fun onSuccess(result: ResultWrapper.Success<BaseResponse>): ResultWrapper.Success<BaseResponse> {
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

    suspend fun build(): ResultWrapper<BaseResponse> {
        loading.invoke(true)
        return safeApiCall2(apiCall, codeRequired).apply {
            end.invoke()
            loading.invoke(false)
        }
    }

    fun buildMerge() {
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
        work.onSuccess(ResultWrapper.Success(BaseResponse()))
    }

    fun merge(vararg build: ResultWrapper<BaseResponse>): RetrofitService {
        listResult = build.toMutableList()
        return this
    }

    private fun isOnline(): Boolean {
        var result = false
        val connectivityManager =
            InjectContext.getContext()!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    private suspend fun safeApiCall2(apiCall: suspend () -> BaseResponse, codeRequired: Any): ResultWrapper<BaseResponse> {
        return withTimeout(Constants.TIME_OUT) {
            if (!isOnline())
                return@withTimeout work.onError(ResultWrapper.Error(ErrorType.NO_INTERNET.code))
            try {
                val response = apiCall.invoke()
                if (codeRequired is Array<*>)
                    codeRequired.forEach {
                        if (response.code == it)
                            return@withTimeout work.onSuccess(ResultWrapper.Success(response))
                    }
                else if (response.code == codeRequired)
                    return@withTimeout work.onSuccess(ResultWrapper.Success(response))

                return@withTimeout work.onError(
                    ResultWrapper.Error(
                        response.code,
                        response.message
                    )
                )
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

    interface Work {
        fun onSuccess(result: ResultWrapper.Success<BaseResponse>): ResultWrapper.Success<BaseResponse>

        fun onError(error: ResultWrapper.Error): ResultWrapper.Error
    }

    init {
        DaggerApiClientComponent.builder().networkModule(ApiClientModule).build().inject(this)
    }
}