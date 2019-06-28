package com.mincor.mvvmclean.domain.usecases.movies

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.common.utils.applyIf
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI

class GetRemoteMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend fun execute(genreId: Int?): SResult<List<MovieUI>> = genreId?.let {
        repository.getRemoteMovies(genreId).applyIf(!repository.hasLocalResults) {
            saveResult(this)
        }.mapListTo()
    } ?: emptyResult()

    private suspend fun saveResult(result: SResult<List<MovieEntity>>) {
        if (result is SResult.Success && result.data.isNotEmpty()) {
            repository.saveMovies(result.data)
        }
    }
}