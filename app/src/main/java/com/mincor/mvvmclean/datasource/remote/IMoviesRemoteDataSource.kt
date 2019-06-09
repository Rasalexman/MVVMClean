package com.mincor.mvvmclean.datasource.remote

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.remote.MovieModel

interface IMoviesRemoteDataSource {

    suspend fun getByGenreId(genreId: Int): SResult<List<MovieModel>>

    suspend fun getMovieDetails(movieId: Int): SResult<MovieModel>

    fun clearPage()
}