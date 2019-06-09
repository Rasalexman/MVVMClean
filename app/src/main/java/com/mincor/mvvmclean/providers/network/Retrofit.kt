package com.mincor.mvvmclean.providers.network

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.mincor.mvvmclean.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private val SERVER_ERROR_CODES = listOf(404, 504, 400, 401, 500, 403)
///-------------- NETWORK CONSTANTS ----////
const val NETWORK_AVAILABLE_AGE = 60
const val NETWORK_UNAVAILABLE_STALE = 60480

fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
    try {
        // Create a trust manager that does not validate certificate chains
        return OkHttpClient.Builder().apply {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            })

            val sslContext = SSLContext.getInstance("SSL").apply {
                init(null, trustAllCerts, SecureRandom())
            }
            sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }
        }
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

fun createOkHttpClient(/*cm: ConnectivityManager?,  cachedDir: File*/): OkHttpClient {
    val httpClient = getUnsafeOkHttpClient()
    httpClient.connectTimeout(60, TimeUnit.SECONDS)
    httpClient.writeTimeout(60, TimeUnit.SECONDS)
    httpClient.readTimeout(60, TimeUnit.SECONDS)

    /*
    try {
        httpClient.cache(Cache(cachedDir, 10L * 1024L * 1024L)) // 10 MB Cache
    } catch (e: Exception) {
        log { "createOkHttpClient Couldn't create http cache because of IO problem." }
    }
    */

    if (BuildConfig.DEBUG) {
        val logging = com.ihsanbal.logging.LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(com.ihsanbal.logging.Level.BODY)
            .log(Platform.INFO)
            //.request("Friendship")
            //.response("Response")
            //.addHeader("version", BuildConfig.VERSION_NAME)
            .build()
        // add logging as last interceptor
        httpClient.addInterceptor(logging)  // <-- this is the important line!
    }

    httpClient.addInterceptor { chain ->
        val request = chain.request()
        val originalHttpUrl = request.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", "026a257e7842ac9cac1fa627496b1468")
            .build()

        /*request = if (isNetworkAvailable(cm)) {
            // if there is connectivity, we tell the request it can reuse the data for sixty seconds.
            request.newBuilder()
                .url(url)
                .removeHeader("Pragma")
                .addHeader("Content-type", "application/json")
                .addHeader("Cache-Control", "public, max-age=$NETWORK_AVAILABLE_AGE")
                .build()

        } else {
            // If there’s no connectivity, we ask to be given only (only-if-cached) ‘stale’ data upto 7 days ago
            request.newBuilder().removeHeader("Pragma").addHeader("Cache-Control", "public, only-if-cached, max-stale=$NETWORK_UNAVAILABLE_STALE").build()
        }*/
        chain.proceed(request.newBuilder()
            .url(url)
            .removeHeader("Pragma")
            .addHeader("Content-type", "application/json")
            .addHeader("Cache-Control", "public, max-age=$NETWORK_AVAILABLE_AGE")
            .build())
    }

    httpClient.addNetworkInterceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        // если получили неизмененный ответ просто выходим
        return@addNetworkInterceptor if (SERVER_ERROR_CODES.contains(originalResponse.code())) {
            return@addNetworkInterceptor originalResponse.newBuilder().code(200).body(originalResponse.body()).build()
        } else originalResponse
    }

    return httpClient.build()
}

fun isNetworkAvailable(cm: ConnectivityManager?): Boolean {
    cm?.let { manager ->
        val activeNetwork: NetworkInfo? = manager.activeNetworkInfo
        return (activeNetwork?.let {
            it.isConnected && it.detailedState == NetworkInfo.DetailedState.CONNECTED
        } ?: false)
    }
    return false
}

inline fun <reified F> createWebServiceApi(okHttpClient: OkHttpClient, url: String): F {
    val moshi = Moshi.Builder().build()
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    return retrofit.create(F::class.java)
}