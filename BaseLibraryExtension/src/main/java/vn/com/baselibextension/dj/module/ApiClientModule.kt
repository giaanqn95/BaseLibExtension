package vn.com.baselibextension.dj.module

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.com.baselibextension.setup_retrofit.ApiInterface
import vn.com.baselibextension.setup_retrofit.UnsafeOkHttpClient


@Module
object ApiClientModule {

    var host: String = "https://mobidev.eximbank.com.vn"
        set(url) {
            field = url.toHttpUrlOrNull()!!.host
        }

    @Provides
    @JvmStatic
    fun client(): Retrofit {
        val httpClient = UnsafeOkHttpClient.unsafeOkHttpClient
        return Retrofit.Builder()
            .baseUrl(host)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @JvmStatic
    fun providePostApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}