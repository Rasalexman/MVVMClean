package com.mincor.mvvmclean.view.uimodels.base

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

abstract class BaseRecyclerUI<M, VH> :
    AbstractItem<M, VH>() where M : AbstractItem<*, *>, VH : FastAdapter.ViewHolder<M> {

    open val viewType = 1
    override fun getType(): Int = viewType
    override fun getLayoutRes(): Int = -1
}