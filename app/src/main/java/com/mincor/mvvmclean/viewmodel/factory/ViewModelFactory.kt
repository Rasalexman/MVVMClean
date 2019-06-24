package com.mincor.mvvmclean.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mincor.mvvmclean.application.MainApplication
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.TT
import org.kodein.di.direct

class ViewModelFactory(appContext: Context) : ViewModelProvider.Factory, KodeinAware {
    override val kodein: Kodein = (appContext as MainApplication).kodein // we can use `by kodein(appContext)` also
    override fun <T : ViewModel> create(modelClass: Class<T>): T = kodein.direct.Instance(TT(modelClass))
}