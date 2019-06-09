package com.mincor.mvvmclean.view.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.mikepenz.fastadapter.items.AbstractItem
import com.mincor.mvvmclean.R
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

abstract class BaseSwipeActionBarRecyclerController : BaseActionBarRecyclerController {

    constructor()
    constructor(args: Bundle) : super(args)

    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun getViewInstance(context: Context):View = BaseBackSwipeRecyclerUI().createView(AnkoContext.create(context, this))

    protected open fun onSwipeRefreshHandler() {}

    override fun addNewItems(list: List<AbstractItem<*, *>>) {
        swipeRefreshLayout?.isRefreshing = false
        super.addNewItems(list)
    }

    override fun showError(error: String?) {
        swipeRefreshLayout?.isRefreshing = false
        super.showError(error)
    }

    override fun onDetach(view: View) {
        swipeRefreshLayout?.isRefreshing = false
        super.onDetach(view)
    }

    override fun onDestroyView(view: View) {
        swipeRefreshLayout?.setOnRefreshListener(null)
        swipeRefreshLayout = null
        super.onDestroyView(view)
    }

    override fun handleBack(): Boolean = true

    private class BaseBackSwipeRecyclerUI : AnkoComponent<BaseSwipeActionBarRecyclerController> {
        override fun createView(ui: AnkoContext<BaseSwipeActionBarRecyclerController>): View = with(ui){
            coordinatorLayout {
                lparams(matchParent, matchParent)
                background = ui.owner.backgroundDrawable

                // свайп
                ui.owner.swipeRefreshLayout = swipeRefreshLayout {
                    onRefresh {
                        ui.owner.onSwipeRefreshHandler()
                    }
                    //----- ЛИСТ С РЕЗУЛЬТАТАМИ ПОИСКА
                    ui.owner.recycler = recyclerView {
                        id = R.id.rv_controller
                        backgroundColor = ui.owner.recyclerBackgroundColor
                    }
                }.lparams(matchParent, matchParent) {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                }
            }
        }
    }
}