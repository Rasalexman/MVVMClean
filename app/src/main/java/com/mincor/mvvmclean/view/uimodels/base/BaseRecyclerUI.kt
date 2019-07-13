package com.mincor.mvvmclean.view.uimodels.base

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

abstract class BaseRecyclerUI<VH> :
    AbstractItem<VH>() where VH : FastAdapter.ViewHolder<*> {
    override val layoutRes: Int = -1
    override val type: Int = 1
}