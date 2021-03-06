package com.mincor.mvvmclean.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.domain.model.base.IConvertableTo
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI

@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val voteCount: Int,
    val voteAverage: Double,
    val isVideo: Boolean,
    val title: String,
    val popularity: Double,
    val posterPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val genreIds: List<Int>,
    val backdropPath: String,
    val releaseDate: Long,
    val adult: Boolean,
    val overview: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val budget: Long,
    var hasDetails: Boolean = false
) : IConvertableTo<MovieUI> {
    override fun convertTo(): MovieUI {
        return MovieUI(
            id = id,
            voteCount = voteCount,
            isVideo = isVideo,
            title = title,
            popularity = popularity,
            posterPath = posterPath,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            genreIds = genreIds,
            backdropPath = backdropPath,
            releaseDate = Consts.UI_DATE_FORMATTER.format(releaseDate),
            adult = adult,
            overview = overview
        )
    }

    fun getBackDropImageUrl() = "${Consts.IMAGES_BACKDROP_URL}$backdropPath"
    fun getImageUrl() = "${Consts.IMAGES_URL}$posterPath"
}