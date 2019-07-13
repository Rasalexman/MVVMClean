package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.domain.repository.GenresRepository
import com.mincor.mvvmclean.domain.repository.MoviesRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val repositoryModule = Kodein.Module("repository_module") {
    bind<GenresRepository>() with singleton { GenresRepository(instance(), instance(), instance()) }
    bind<MoviesRepository>() with singleton { MoviesRepository(instance(), instance()) }
}