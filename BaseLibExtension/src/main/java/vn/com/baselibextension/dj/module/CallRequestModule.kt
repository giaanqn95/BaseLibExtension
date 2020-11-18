package vn.com.baselibextension.dj.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import vn.com.baselibextension.setup_retrofit.RetrofitService

@Module
object CallRequestModule {

    @Provides
    @Reusable
    @JvmStatic
    fun getRetroService(): RetrofitService {
        return RetrofitService()
    }
}