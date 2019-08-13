package com.mincor.mvvmclean.domain.usecases.movies

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI

class GetNewMoviesUseCase (
    val repository: MoviesRepository
) {
    suspend fun execute(genreId: Int?): SResult<List<MovieUI>> = genreId?.let {
        repository.getNewRemoteMovies(genreId).also {
            saveResult(it)
        }.mapListTo()
    } ?: emptyResult()

    private suspend fun saveResult(result: SResult<List<MovieEntity>>) {
        if (result is SResult.Success && result.data.isNotEmpty()) {
            repository.saveMovies(result.data)
        }
    }
}