package vn.com.baselibextension.setup_retrofit

import android.content.Context


/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 10:36 AM
 */

class RetrofitService<T>(val context: Context, val value: T) {


    private lateinit var processResponse: Process<T>
    private var listResult: MutableList<ResultWrapper<T>> = ArrayList()
    private var request: Request<T> = Request()

    var end: () -> Unit = {}
    var loading: (isLoading: Boolean) -> Unit = {}
    var work: Work<T> = object : Work<T> {
        override fun onSuccess(result: ResultWrapper.Success<T>): ResultWrapper.Success<T> =
            result

        override fun onError(error: ResultWrapper.Error): ResultWrapper.Error = error
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

    fun merge(vararg build: ResultWrapper<T>) = apply {
        listResult = build.toMutableList()
        return this
    }

    fun merge(build: MutableList<ResultWrapper<T>>) = apply {
        listResult = build
        return this
    }

    suspend fun build(repo: Repo, request: Request<T>): ResultWrapper<T> {
        val process =  RequestProcess(repo,request, processResponse, context)
        process.request.loading.invoke(true)
        return process.safeApiCall().apply {
            process.request.end.invoke()
            process.request.loading.invoke(false)
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
        work.onSuccess(ResultWrapper.Success(value))
    }

    interface Process<T> {
        fun process(response: String, codeRequire: Any?): ResultWrapper<T>
    }

    interface Work<T> {
        fun onSuccess(result: ResultWrapper.Success<T>): ResultWrapper.Success<T>

        fun onError(error: ResultWrapper.Error): ResultWrapper.Error
    }
}