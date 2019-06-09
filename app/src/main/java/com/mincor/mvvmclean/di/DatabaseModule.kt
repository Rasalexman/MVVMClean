package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.providers.database.MoviesDatabase
import com.mincor.mvvmclean.providers.database.dao.IGenresDao
import com.mincor.mvvmclean.providers.database.dao.IMoviesDao
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val databaseModule = Kodein.Module("database_module") {
    bind<IGenresDao>() with provider { kodein.direct.instance<MoviesDatabase>().getGenresDao() }
    bind<IMoviesDao>() with provider { kodein.direct.instance<MoviesDatabase>().getMoviesDao() }
}