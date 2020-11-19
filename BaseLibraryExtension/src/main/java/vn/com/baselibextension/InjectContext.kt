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

    var context: Context? = null

    companion object {
        var instance = InjectContext()
        var domain: String = ""

        fun getRetro(): RetrofitService {
            return instance.retrofitService
        }

        fun getContext(): Context? {
            return instance.context
        }

        fun setContext(context: Context): InjectContext {
            this.instance.context = context
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