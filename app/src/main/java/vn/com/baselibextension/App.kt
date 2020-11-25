package vn.com.baselibextension

import android.app.Application
import vn.com.baselibextension.dj.module.ApiClientModule
import vn.com.baselibextension.setup_retrofit.BaseResponse
import vn.com.baselibextension.setup_retrofit.JSON
import vn.com.baselibextension.setup_retrofit.ResultWrapper
import vn.com.baselibextension.setup_retrofit.RetrofitService

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClientModule.host = "https://api.islp.dev.intelin.vn"
        InjectContet.initRetroService(this)
        InjectContet.getRetro().setProcessResponse(object : RetrofitService.Process<BaseResponse> {
            override fun process(response: String, codeRequire: Any): ResultWrapper<BaseResponse> {
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