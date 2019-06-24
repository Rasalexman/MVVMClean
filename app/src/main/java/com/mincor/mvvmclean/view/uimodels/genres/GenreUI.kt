package com.mincor.mvvmclean.view.uimodels.genres

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.mincor.mvvmclean.view.viewholders.GenreViewHolder
import com.mincor.mvvmclean.view.uimodels.base.BaseRecyclerUI
import org.jetbrains.anko.AnkoContext

data class GenreUI(val id: Int, val name: String) : BaseRecyclerUI<GenreUI, GenreViewHolder>() {
    override fun createView(ctx: Context, parent: ViewGroup?): View = GenreViewHolder.createView(AnkoContext.Companion.create(ctx, this))
    override fun getViewHolder(v: View) = GenreViewHolder(v)
}

