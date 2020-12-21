package vn.com.baselibextension

import android.content.Context
import vn.com.baselibextension.base.Repo
import vn.com.baselibextension.base.Request
import vn.com.baselibextension.base.RequestProcess
import vn.com.baselibextension.base.ResultWrapper


/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 10:36 AM
 */

class RetrofitService<T>(val context: Context) {

    private lateinit var processResponse: Process<T>
    private var listResult: MutableList<suspend () -> ResultWrapper<T>> = ArrayList()

    var end: () -> Unit = {}
    var loading: (isLoading: Boolean) -> Unit = {}
    var work: Work = object : Work {
        override fun onSuccess() {}

        override fun onError(error: String) {}
    }

    inline fun work(
        crossinline onSuccess: () -> Unit = {},
        crossinline onError: (error: String) -> Unit = {}
    ) = work(object : Work {
        override fun onSuccess() {
            onSuccess.invoke()
        }

        override fun onError(error: String) {
            onError.invoke(error)
        }
    })

    fun work(work: Work) = apply {
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

    fun merge(vararg build: suspend () -> ResultWrapper<T>) = apply {
        listResult = build.toMutableList()
        return this
    }

    fun merge(build: MutableList<suspend () -> ResultWrapper<T>>) = apply {
        listResult = build
        return this
    }

    suspend fun build(repo: Repo, request: Request<T>): ResultWrapper<T> {
        val process = RequestProcess(repo, request, processResponse, context)
        process.request.loading.invoke(true)
        return process.safeApiCall().apply {
            process.request.end.invoke()
            process.request.loading.invoke(false)
        }
    }

    suspend fun buildMerge() {
        loading.invoke(true)
        listResult.forEach {
            if (it.invoke() !is ResultWrapper.Success) {
                work.onError("${(it.invoke() as ResultWrapper.Error).message}")
                end.invoke()
                loading.invoke(false)
                return
            }
        }
        loading.invoke(false)
        work.onSuccess()
    }

    interface Process<T> {
        fun process(response: String, codeRequire: Any?): ResultWrapper<T>
    }

    interface Work {
        fun onSuccess()

        fun onError(error: String)
    }
}