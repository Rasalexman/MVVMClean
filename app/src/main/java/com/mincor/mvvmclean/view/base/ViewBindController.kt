package com.mincor.mvvmclean.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.mincor.mvvmclean.common.utils.clear
import com.mincor.mvvmclean.common.utils.log
import com.mincor.room.utils.clearAfterDestroyView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.kodein

abstract class ViewBindController : LifecycleController, KodeinAware {

    override val kodein: Kodein by lazy {
        (applicationContext as KodeinAware).kodein
    }
    constructor()
    constructor(args: Bundle) : super(args)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val viewInstance = getViewInstance(inflater.context)
        onViewCreated(viewInstance)
        return viewInstance
    }

    abstract fun getViewInstance(context: Context):View
    abstract fun onViewCreated(view: View)

    override fun onDestroyView(view: View) {
        (view as? ViewGroup)?.clear()
        super.onDestroyView(view)
        clearAfterDestroyView<ViewBindController>()
    }
}
