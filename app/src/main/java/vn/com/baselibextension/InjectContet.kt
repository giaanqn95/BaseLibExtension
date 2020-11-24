package vn.com.baselibextension

import android.content.Context
import vn.com.baselibextension.setup_retrofit.BaseResponse
import vn.com.baselibextension.setup_retrofit.RetrofitService

/**
 * Created by giaan on 11/24/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class InjectContet {
    private lateinit var retrofitService: RetrofitService<BaseResponse>

    companion object {
        val instance by lazy {
            InjectContet()
        }

        fun getRetro(): RetrofitService<BaseResponse> {
            return instance.retrofitService
        }

        fun initRetroService(context: Context) = apply {
            this.instance.retrofitService = RetrofitService(context)
        }
    }
}