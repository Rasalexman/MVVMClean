package com.mincor.mvvmclean.common.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager


@com.bumptech.glide.annotation.GlideModule
class MyAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize

        //val memoryCacheSizeBytes = 1024 * 1024 * 200 // 200mb
        builder.setMemoryCache(LruResourceCache(defaultMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(defaultBitmapPoolSize.toLong()))

        /*
        // or any other path
        val downloadDirectoryPath = Environment.getDownloadCacheDirectory().path
        builder.setDiskCache(
                new DiskLruCacheFactory(context, cacheSize100MegaBytes)
        );*/

        // set size & external vs. internal
        val cacheSize200MegaBytes = 1024 * 1024 * 200 // 200mb
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, "my_app_cache", cacheSize200MegaBytes.toLong()))

        //builder?.setLogLevel(Log.VERBOSE)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        unsafeOkHttpClient().let {
            registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(it))
        }
    }

    private fun unsafeOkHttpClient(): OkHttpClient {
        val unsafeTrustManager = createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)
        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    private fun createUnsafeTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<out X509Certificate>? {
                return emptyArray()
            }
        }
    }
}