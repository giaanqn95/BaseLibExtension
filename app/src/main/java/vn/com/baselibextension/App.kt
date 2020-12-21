package vn.com.baselibextension

import android.app.Application
import vn.com.baselibextension.InjectContext.initRetroService
import vn.com.baselibextension.setup_retrofit.ApiClientModule

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClientModule.host = "https://api.tiktik.dev.intelin.vn"
        initRetroService(this)
    }
}