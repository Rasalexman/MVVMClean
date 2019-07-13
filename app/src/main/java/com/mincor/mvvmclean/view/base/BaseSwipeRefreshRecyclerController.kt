package com.mincor.mvvmclean.view.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mikepenz.fastadapter.items.AbstractItem
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

abstract class BaseSwipeRefreshRecyclerController : BaseRecyclerController {

    constructor()
    constructor(args: Bundle) : super(args)

    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun getViewInstance(context: Context): View =
        SwipeRefreshRecyclerUI().createView(AnkoContext.create(context, this))

    protected abstract fun onSwipeToRefreshHandler()

    override fun addNewItems(list: List<AbstractItem<*>>) {
        swipeRefreshLayout?.isRefreshing = false
        super.addNewItems(list)
    }

    override fun onDetach(view: View) {
        swipeRefreshLayout?.isRefreshing = false
        super.onDetach(view)
    }

    override fun addItems(list: List<AbstractItem<*>>) {
        swipeRefreshLayout?.isRefreshing = false
        super.addItems(list)
    }

    override fun onDestroyView(view: View) {
        swipeRefreshLayout?.setOnRefreshListener(null)
        swipeRefreshLayout = null
        super.onDestroyView(view)
    }

    override fun showError(error: String?) {
        swipeRefreshLayout?.isRefreshing = false
        super.showError(error)
    }

    private inner class SwipeRefreshRecyclerUI : AnkoComponent<BaseSwipeRefreshRecyclerController> {
        override fun createView(ui: AnkoContext<BaseSwipeRefreshRecyclerController>): View = with(ui) {
            swipeRefreshLayout = swipeRefreshLayout {
                onRefresh {
                    onSwipeToRefreshHandler()
                }
                recycler = recyclerView {
                    lparams(matchParent, matchParent)
                }
            }
            swipeRefreshLayout!!
        }
    }
}