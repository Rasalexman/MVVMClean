package com.mincor.mvvmclean.view.base

import android.os.Bundle
import android.view.View
import com.mincor.mvvmclean.view.base.BaseActionBarController

abstract class BaseBackButtonController : BaseActionBarController {

    protected open val isSetBackButton = true

    constructor()
    constructor(args: Bundle) : super(args)

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
        setHasOptionsMenu(true)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        if(isSetBackButton) setHomeButtonEnable()
    }
}
