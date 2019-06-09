package com.mincor.mvvmclean.common.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule


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
}