package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.datasource.remote.GenresRemoteDataSource
import com.mincor.mvvmclean.datasource.remote.IGenresRemoteDataSource
import com.mincor.mvvmclean.datasource.remote.IMoviesRemoteDataSource
import com.mincor.mvvmclean.datasource.remote.MoviesRemoteDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val remoteDataSourceModule = Kodein.Module("remote_data_source_module") {
    bind<IGenresRemoteDataSource>() with singleton { GenresRemoteDataSource(instance()) }
    bind<IMoviesRemoteDataSource>() with singleton { MoviesRemoteDataSource(instance()) }
}