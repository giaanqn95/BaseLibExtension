package vn.com.baselibextension

import android.content.Context
import vn.com.baselibextension.dj.component.DaggerCallRequestComponent
import vn.com.baselibextension.dj.module.CallRequestModule
import vn.com.baselibextension.setup_retrofit.RetrofitService
import javax.inject.Inject

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class InjectContext {

    init {
        init()
    }

    @Inject
    lateinit var retrofitService: RetrofitService

    companion object {
        var instance = InjectContext()
        var domain: String = ""
        var context: Context? = null

        fun getRetro(): RetrofitService {
            return instance.retrofitService
        }

        @JvmName("getContext1")
        fun getContext(): Context? {
            return context
        }

        fun setContext(context: Context): InjectContext {
            this.context = context
            return this.instance
        }

        fun setDomain(domain: String): InjectContext {
            this.domain = domain
            return this.instance
        }
    }

    private fun init() {
        DaggerCallRequestComponent.builder().callRequestModule(CallRequestModule).build()
            .inject(this)
    }
}