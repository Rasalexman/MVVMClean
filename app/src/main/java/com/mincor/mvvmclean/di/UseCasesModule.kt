package com.mincor.mvvmclean.di

import com.mincor.mvvmclean.domain.usecases.details.GetLocalDetailsUseCase
import com.mincor.mvvmclean.domain.usecases.details.GetRemoteDetailsUseCase
import com.mincor.mvvmclean.domain.usecases.genres.GetGenresUseCase
import com.mincor.mvvmclean.domain.usecases.genres.GetLocalGenresUseCase
import com.mincor.mvvmclean.domain.usecases.genres.GetRemoteGenresUseCase
import com.mincor.mvvmclean.domain.usecases.movies.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val useCasesModule = Kodein.Module("usecases_module") {

    //---- Single Use-cases
    bind<GetLocalDetailsUseCase>() with provider {
        GetLocalDetailsUseCase(
            instance()
        )
    }
    bind<GetRemoteDetailsUseCase>() with provider {
        GetRemoteDetailsUseCase(
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
    bind<GetLocalGenresUseCase>() with provider {
        GetLocalGenresUseCase(
            instance()
        )
    }
    bind<GetRemoteGenresUseCase>() with provider {
        GetRemoteGenresUseCase(
            instance()
        )
    }
    bind<GetNewMoviesUseCase>() with provider {
        GetNewMoviesUseCase(
            instance()
        )
    }

    //---- Combined Use-cases
    bind<GetGenresUseCase>() with provider {
        GetGenresUseCase(
            instance(),
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
            instance(),
            instance()
        )
    }
}