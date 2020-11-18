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
    var domain: String = ""

    companion object {
        var instance = InjectContext()
    }

    private fun init() {
        DaggerCallRequestComponent.builder().callRequestModule(CallRequestModule).build()
            .inject(this)
    }
}