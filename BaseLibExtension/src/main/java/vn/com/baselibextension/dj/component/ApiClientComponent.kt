package vn.com.baselibextension.dj.component

import dagger.Component
import vn.com.baselibextension.dj.module.ApiClientModule
import vn.com.baselibextension.setup_retrofit.RetrofitService
import javax.inject.Singleton

/**
 * Copyright by Intelin.
 * Creator: antdg-intelin
 * Date: 24/02/2020
 * Time: 13:30
 */

@Singleton
@Component(modules = [ApiClientModule::class])
interface ApiClientComponent {

    fun inject(retrofitService: RetrofitService)

    @Component.Builder
    interface Builder {
        fun build(): ApiClientComponent

        fun networkModule(networkModule: ApiClientModule): Builder

    }
}