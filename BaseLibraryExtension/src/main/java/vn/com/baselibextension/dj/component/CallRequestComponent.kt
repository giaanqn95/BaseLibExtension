package vn.com.baselibextension.dj.component

import dagger.Component
import vn.com.baselibextension.InjectContext
import vn.com.baselibextension.dj.module.CallRequestModule
import javax.inject.Singleton

@Singleton
@Component(modules = [CallRequestModule::class])
interface CallRequestComponent {

    fun inject(baseViewModel: InjectContext)

    @Component.Builder
    interface Builder {
        fun build(): CallRequestComponent

        fun callRequestModule(callRequestProvider: CallRequestModule): Builder
    }
}