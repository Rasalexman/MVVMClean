package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.providers.network.api.IMovieApi
import com.mincor.mvvmclean.providers.network.createOkHttpClient
import com.mincor.mvvmclean.providers.network.createWebServiceApi
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with

private const val TAG_SERVER_URL = "server_url_const"

val netModule = Kodein.Module("netModule") {
    constant(TAG_SERVER_URL) with Consts.SERVER_URL
    bind<OkHttpClient>() with singleton { createOkHttpClient() }
    bind<IMovieApi>() with singleton { createWebServiceApi<IMovieApi>(instance(), instance(TAG_SERVER_URL)) }
}