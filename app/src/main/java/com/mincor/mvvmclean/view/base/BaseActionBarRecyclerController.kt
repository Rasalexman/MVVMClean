package com.mincor.mvvmclean.view.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.mincor.mvvmclean.R
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.verticalLayout

/**
 * Created by a.minkin on 20.10.2017.
 */
open class BaseActionBarRecyclerController : BaseRecyclerController {

    constructor()
    constructor(args: Bundle) : super(args)

    override fun getViewInstance(context: Context):View = ToolbarRecyclerUI().createView(AnkoContext.create(context, this))

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

    private class ToolbarRecyclerUI : AnkoComponent<BaseActionBarRecyclerController> {
        override fun createView(ui: AnkoContext<BaseActionBarRecyclerController>): View = with(ui){
            verticalLayout {
                background = ui.owner.backgroundDrawable

                lparams(org.jetbrains.anko.matchParent, org.jetbrains.anko.matchParent)
                //----- ЛИСТ С РЕЗУЛЬТАТАМИ ПОИСКА
                ui.owner.recycler = recyclerView {
                    id = R.id.rv_controller
                    backgroundColor = ui.owner.recyclerBackgroundColor
                    lparams(org.jetbrains.anko.matchParent, org.jetbrains.anko.matchParent)
                }
            }
        }
    }
}