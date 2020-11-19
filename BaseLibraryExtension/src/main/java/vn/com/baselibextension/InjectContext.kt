package vn.com.baselibextension

import android.content.Context
import vn.com.baselibextension.setup_retrofit.RetrofitService

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class InjectContext {

    init {
        init()
    }

    var retrofitService: RetrofitService? = null
    var context: Context? = null
    var domain: String = ""

    companion object {
        val instance by lazy {
            InjectContext()
        }


        fun getRetro(): RetrofitService {
            if(instance.retrofitService == null)
                instance.retrofitService = instance.context?.let { RetrofitService(it) }
            return instance.retrofitService!!
        }

        fun getContext(): Context? {
            return instance.context
        }

        fun setContext(context: Context): InjectContext {
            this.instance.context = context
            return this.instance
        }

        fun setDomain(domain: String): InjectContext {
            this.instance.domain = domain
            return this.instance
        }
    }

    private fun init() {
//        DaggerCallRequestComponent.builder().callRequestModule(CallRequestModule).build()
//            .inject(this)
    }
}