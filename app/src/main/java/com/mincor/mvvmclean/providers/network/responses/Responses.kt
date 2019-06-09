package com.mincor.mvvmclean.providers.network.responses

import com.mincor.mvvmclean.domain.model.remote.GenreModel
import com.mincor.mvvmclean.domain.model.remote.MovieModel

data class GetGenresResponse(val genres: List<GenreModel>)
data class GetMoviesByGenreIdResponse(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<MovieModel>
)