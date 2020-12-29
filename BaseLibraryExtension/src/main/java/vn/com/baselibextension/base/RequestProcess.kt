package vn.com.baselibextension.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.MultipartBody
import retrofit2.HttpException
import vn.com.baselibextension.setup_retrofit.ApiClientModule
import vn.com.baselibextension.setup_retrofit.ApiInterface
import vn.com.baselibextension.utils.Constants
import vn.com.baselibextension.utils.JSON

/**
 * Created by giaan on 12/16/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class RequestProcess<T>(val repo: Repo, val request: Request<T>, var processResponse: RetrofitService.Process<T>, val context: Context) {

    lateinit var apiCall: (suspend () -> T)
    var codeRequired: Any? = repo.codeRequired

    init {
        request()
    }

    private var apiInterface: ApiInterface = ApiClientModule.providePostApi()

    private suspend fun getMethod(headers: Map<String, String>, url: String, message: Any? = null) {
        this.apiCall = { apiInterface.get(headers, url + message) }
    }

    private suspend fun postMethod(headers: Map<String, String>, url: String, message: Any? = null) = apply {
        this.apiCall = { apiInterface.post(headers, url, message) }
        return this
    }

    private suspend fun putMethod(headers: Map<String, String>, url: String, message: Any? = null) {
        this.apiCall = { apiInterface.put(headers, url, message) }
    }

    private suspend fun deleteMethod(headers: Map<String, String>, url: String, message: Any?) {
        this.apiCall = { apiInterface.delete(headers, url, message) }
    }

    private suspend fun uploadFile(url: String, message: MultipartBody.Part?) {
        this.apiCall = { apiInterface.uploadFile(url, message) }
    }

    fun request() = CoroutineScope(Dispatchers.IO).launch {
        when (repo.typeRepo) {
            TypeRepo.GET -> {
                getMethod(repo.headers, repo.url, repo.message)
            }
            TypeRepo.POST -> {
                postMethod(repo.headers, repo.url, repo.message)
            }
            TypeRepo.PUT -> {
                putMethod(repo.headers, repo.url, repo.message)
            }
            TypeRepo.DELETE -> {
                deleteMethod(repo.headers, repo.url, repo.message)
            }
            TypeRepo.POST_MULTIPART -> {
                uploadFile(repo.url, repo.multiPart)
            }
        }
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

    suspend fun safeApiCall(): ResultWrapper<T> {
        return withTimeout(Constants.TIME_OUT) {
            if (!isOnline())
                return@withTimeout request.work.onError(ResultWrapper.Error("NoInternet"))
            try {
                val response = apiCall.invoke()
                val process = processResponse.process(JSON.encode(response), codeRequired)
                if (process is ResultWrapper.Success<T>) {
                    request.work.onSuccess(ResultWrapper.Success(process.value))
                } else {
                    request.work.onError(ResultWrapper.Error((process as ResultWrapper.Error).code, process.message))
                }
                return@withTimeout process
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        val code = throwable.code()
                        if (code in 500..599) {
                            request.work.onError(ResultWrapper.Error("$code", throwable.message))
                            return@withTimeout ResultWrapper.Error("$code", throwable.message)
                        } else if (code in 400..499) {
                            request.work.onError(ResultWrapper.Error("$code", throwable.message))
                            return@withTimeout ResultWrapper.Error("$code", throwable.message)
                        }
                        request.work.onError(ResultWrapper.Error("$code", throwable.message))
                        return@withTimeout ResultWrapper.Error("$code", throwable.message)
                    }
                    else -> {
                        request.work.onError(ResultWrapper.Error("", throwable.message))
                        return@withTimeout ResultWrapper.Error("", throwable.message)
                    }
                }
            }
        }
    }
}