package vn.com.baselibextension.setup_retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import vn.com.baselibextension.utils.Constants
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by giaan on 10/16/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class UnsafeOkHttpClient {

    companion object {
        val unsafeOkHttpClient: OkHttpClient
            get() = try {
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                val sslSocketFactory = sslContext.socketFactory
                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, (trustAllCerts[0] as X509TrustManager))
                builder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.protocols(Collections.singletonList(Protocol.HTTP_1_1))
                builder.addInterceptor(logging)
                    .callTimeout(ApiClientModule.timeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(ApiClientModule.timeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(ApiClientModule.timeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(ApiClientModule.timeOut, TimeUnit.MILLISECONDS)
                builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
    }
}