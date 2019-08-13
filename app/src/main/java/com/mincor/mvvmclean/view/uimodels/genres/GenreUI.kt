package com.mincor.mvvmclean.view.uimodels.genres

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.mincor.mvvmclean.view.uimodels.base.BaseRecyclerUI
import com.mincor.mvvmclean.view.viewholders.GenreViewHolder
import org.jetbrains.anko.AnkoContext

data class GenreUI(val id: Int, val name: String) : BaseRecyclerUI<GenreViewHolder>() {

    init {
        identifier = id.toLong()
    }

    override fun createView(ctx: Context, parent: ViewGroup?): View =
        GenreViewHolder.createView(AnkoContext.Companion.create(ctx, this))
    override fun getViewHolder(v: View) = GenreViewHolder(v)
}

