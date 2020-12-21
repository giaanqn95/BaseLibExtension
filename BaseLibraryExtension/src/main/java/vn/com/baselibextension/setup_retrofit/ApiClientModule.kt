package vn.com.baselibextension.setup_retrofit

import dagger.Module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
object ApiClientModule {

    var host = ""
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