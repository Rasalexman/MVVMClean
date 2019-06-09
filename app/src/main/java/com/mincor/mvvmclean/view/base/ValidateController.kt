package com.mincor.mvvmclean.view.base

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.mincor.mvvmclean.view.base.BaseController

abstract class ValidateController : BaseController {

    constructor()
    constructor(args: Bundle) : super(args)

    protected var cancel = false
    protected var focusView: View? = null

    protected fun errorApplyHandler(errorStr:String, errorObject: EditText?) {
        errorObject?.error = errorStr
        focusView = errorObject
        cancel = true
    }

    protected open fun resetErrors() {
        cancel = false
        focusView = null
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        resetErrors()
    }
}