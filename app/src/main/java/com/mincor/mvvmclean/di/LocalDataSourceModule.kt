package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.datasource.local.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val localDataSourceModule = Kodein.Module("local_data_source_module") {
    bind<IGenresLocalDataSource>() with singleton { GenresLocalDataSource(instance()) }
    bind<IGenresCacheDataSource>() with singleton { GenresCacheDataSource() }
    bind<IMoviesLocalDataSource>() with singleton { MoviesLocalDataSource(instance()) }
}