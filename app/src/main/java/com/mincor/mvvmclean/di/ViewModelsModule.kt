package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.viewmodel.GenresViewModel
import com.mincor.mvvmclean.viewmodel.MovieDetailViewModel
import com.mincor.mvvmclean.viewmodel.MoviesViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelsModule = Kodein.Module("view_models_module") {
    bind<GenresViewModel>() with provider { GenresViewModel(instance()) }
    bind<MoviesViewModel>() with provider { MoviesViewModel(instance()) }
    bind<MovieDetailViewModel>() with provider { MovieDetailViewModel(instance()) }
}