package com.mincor.mvvmclean.view.base

import android.os.Bundle
import android.view.View


/**
 * Created by alexander on 24.08.17.
 */

abstract class BaseActionBarController : ValidateController {

    protected constructor()
    protected constructor(args: Bundle) : super(args)

    override fun onAttach(view: View) {
        super.onAttach(view)
        createToolBar()
        setActionBar()
        setTitle()
    }

    protected fun setHomeButtonEnable() {
        //set the back arrow in the toolBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /*override fun onChangeStarted(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeStarted(changeHandler, changeType)
        val hidden = !changeType.isEnter
        setOptionsMenuHidden(hidden)
    }*/
}
