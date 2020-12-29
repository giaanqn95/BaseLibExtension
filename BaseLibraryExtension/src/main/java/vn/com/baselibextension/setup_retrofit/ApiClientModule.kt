package vn.com.baselibextension.setup_retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ApiClientModule {

    var host = ""
    var timeOut = 15000L

    @JvmName("setTimeOut1")
    fun setTimeOut(timeOut: Long) = apply {
        this.timeOut = timeOut
    }

    fun setUrl(url: String) = apply {
        this.host = url
    }

    fun client(): Retrofit {
        val httpClient = UnsafeOkHttpClient.unsafeOkHttpClient
        return Retrofit.Builder()
            .baseUrl(host)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    fun providePostApi(): ApiInterface {
        return client().create(ApiInterface::class.java)
    }
}