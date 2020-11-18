package vn.com.baselibextension.utils

import android.text.TextUtils
import android.util.Log

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 3:12 PM
 */
object LogCat {
    //LogHusky Debug
    fun d(_msg: String): Int {
        return d(_msg, 5)
    }

    private fun d(_msg: String, deepth: Int): Int {
        return if (TextUtils.isEmpty(_msg)) Log.e(
            LogHusky(deepth, TAG.DEBUG),
            "LogHusky message parameter is empty!!"
        ) else Log.d(
            LogHusky(deepth, TAG.DEBUG), _msg
        )
    }

    //LogHusky Info
    fun i(_msg: String): Int {
        return i(_msg, 5)
    }

    private fun i(_msg: String, deepth: Int): Int {
        return if (TextUtils.isEmpty(_msg)) Log.e(
            LogHusky(deepth, TAG.INFO),
            "LogHusky message parameter is empty!!"
        ) else Log.d(
            LogHusky(deepth, TAG.INFO), _msg
        )
    }

    //LogHusky Error
    fun e(e: Exception?) {
        if (null != e) {
            e(e.message, 5)
            e.printStackTrace()
        }
    }

    fun e(t: Throwable?) {
        if (null != t) {
            e(t.message, 5)
            t.printStackTrace()
        }
    }

    fun e(_msg: String?): Int {
        return e(_msg, 5)
    }

    fun e(_msg: String?, deepth: Int): Int {
        return if (TextUtils.isEmpty(_msg)) Log.e(
            LogHusky(deepth, TAG.ERROR),
            "LogHusky message parameter is empty!!"
        ) else Log.d(
            LogHusky(deepth, TAG.ERROR),
            _msg!!
        )
    }

    private fun LogHusky(depth: Int, TAG: String): String {
        val element = Thread.currentThread().stackTrace[depth]
        val str = element.fileName
        return TAG + "[" + element.methodName + "]-[" + str + ":" + element.lineNumber + "]"
    }

    private object TAG {
        const val DEBUG = "-[DEBUG]-"
        const val ERROR = "-[ERROR]-"
        const val INFO = "-[INFO]-"
    }
}