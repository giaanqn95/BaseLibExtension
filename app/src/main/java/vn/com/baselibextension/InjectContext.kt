package vn.com.baselibextension

import android.content.Context
import vn.com.baselibextension.base.ResultWrapper
import vn.com.baselibextension.utils.JSON

/**
 * Created by giaan on 11/24/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */

object InjectContext {

    lateinit var retrofitService: RetrofitService<BaseResponse>

    fun initRetroService(context: Context) {
        retrofitService = RetrofitService(context)
        retrofitService.setProcessResponse(object : RetrofitService.Process<BaseResponse> {
            override fun process(response: String, codeRequire: Any?): ResultWrapper<BaseResponse> {
                val parse = JSON.decode(response, BaseResponse::class.java)
                if (codeRequire is Array<*>) {
                    codeRequire.forEach {
                        if (parse?.code == it)
                            return ResultWrapper.Success(parse!!)
                    }
                }
                if (parse!!.code == codeRequire)
                    return ResultWrapper.Success(parse)
                return ResultWrapper.Error(parse.code, parse.message)
            }
        })
    }
}