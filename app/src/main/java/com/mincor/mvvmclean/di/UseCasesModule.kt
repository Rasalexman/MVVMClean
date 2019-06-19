package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.domain.usecases.genres.GetGenresUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetCachedMoviesUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetMovieDetailUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetMoviesUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetRemoteMoviesUseCase
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
    bind<GetMoviesUseCase>() with provider {
        GetMoviesUseCase(
            instance(),
            instance(),
            instance()
        )
    }
    bind<GetMovieDetailUseCase>() with provider {
        GetMovieDetailUseCase(
            instance()
        )
    }
    bind<GetCachedMoviesUseCase>() with provider {
        GetCachedMoviesUseCase(
            instance()
        )
    }
    bind<GetRemoteMoviesUseCase>() with provider {
        GetRemoteMoviesUseCase(
            instance()
        )
    }
}