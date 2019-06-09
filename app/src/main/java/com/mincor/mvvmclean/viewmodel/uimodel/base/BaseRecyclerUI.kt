package com.mincor.mvvmclean.viewmodel.uimodel.base

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

abstract class BaseRecyclerUI<M, VH> :
    AbstractItem<M, VH>() where M : AbstractItem<*, *>, VH : FastAdapter.ViewHolder<M> {

    open val viewType = 1
    override fun getType(): Int = viewType
    override fun getLayoutRes(): Int = -1
}