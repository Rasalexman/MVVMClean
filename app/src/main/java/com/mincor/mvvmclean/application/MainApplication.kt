package com.mincor.mvvmclean.application

import android.app.Application
import androidx.room.Room
import com.mincor.mvvmclean.providers.database.MoviesDatabase
import com.mincor.mvvmclean.di.*
import com.mincor.mvvmclean.viewmodel.factory.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class MainApplication : Application(), KodeinAware {

    override val kodein: Kodein by Kodein.lazy {
        bind<ViewModelFactory>() with singleton { ViewModelFactory(applicationContext) }
        bind<MoviesDatabase>() with singleton {
            Room.databaseBuilder(
                applicationContext,
                MoviesDatabase::class.java, "moviesDB"
            ).build()
        }

        import(androidXModule(this@MainApplication))
        import(netModule)
        import(databaseModule)
        import(localDataSourceModule)
        import(remoteDataSourceModule)
        import(repositoryModule)
        import(useCasesModule)
        import(viewModelsModule)
    }
}