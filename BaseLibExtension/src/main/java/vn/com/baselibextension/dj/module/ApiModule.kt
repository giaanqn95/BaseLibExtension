package vn.com.baselibextension.dj.module

import dagger.Module
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */

@Module
object ApiModule {

    private var scheme: String = ""
        set(url) {
            field = url.toHttpUrlOrNull()!!.scheme
        }


    private var host: String = ""
        set(url) {
            field = url.toHttpUrlOrNull()!!.host
        }

}