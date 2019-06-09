package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.domain.usecases.genres.GetGenresUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetMoviesUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetMovieDetailUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetNextMoviesUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val useCasesModule = Kodein.Module("usecases_module") {
    bind<GetGenresUseCase>() with provider {
        GetGenresUseCase(
            instance()
        )
    }
    bind<GetNextMoviesUseCase>() with provider {
        GetNextMoviesUseCase(
            instance()
        )
    }
    bind<GetMoviesUseCase>() with provider {
        GetMoviesUseCase(
            instance()
        )
    }
    bind<GetMovieDetailUseCase>() with provider {
        GetMovieDetailUseCase(
            instance()
        )
    }
}