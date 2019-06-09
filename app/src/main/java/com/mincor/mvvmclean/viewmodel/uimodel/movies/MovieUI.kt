package com.mincor.mvvmclean.viewmodel.uimodel.movies

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.view.viewholders.MovieViewHolder
import com.mincor.mvvmclean.viewmodel.uimodel.base.BaseRecyclerUI
import org.jetbrains.anko.AnkoContext

data class MovieUI(
    val id: Int,
    val voteCount: Int,
    val isVideo: Boolean,
    val title: String,
    val popularity: Double,
    val posterPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val genreIds: List<Int>,
    val backdropPath: String,
    val releaseDate: String,
    val adult: Boolean,
    val overview: String
) : BaseRecyclerUI<MovieUI, MovieViewHolder>() {
    override fun createView(ctx: Context, parent: ViewGroup?): View =
        MovieViewHolder.createView(AnkoContext.Companion.create(ctx, this))
    override fun getViewHolder(v: View) = MovieViewHolder(v)

    init {
        withIdentifier(id.toLong())
    }

    val fullPosterUrl: String
        get() = "${Consts.IMAGES_URL}$posterPath"
}