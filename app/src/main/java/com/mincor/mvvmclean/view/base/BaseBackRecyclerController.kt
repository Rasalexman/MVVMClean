package com.mincor.mvvmclean.view.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.mincor.mvvmclean.view.base.BaseActionBarRecyclerController

abstract class BaseBackRecyclerController : BaseActionBarRecyclerController {

    constructor()
    constructor(args: Bundle) : super(args)

    init {
        setHasOptionsMenu(true)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        setHomeButtonEnable()
    }

    //handle the click on the back arrow click
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    goBack()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}