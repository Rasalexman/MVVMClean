package com.mincor.mvvmclean.domain.usecases.movies

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.utils.applyIf
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository

class GetRemoteMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend fun execute(genreId: Int?): SResult<List<MovieEntity>> = genreId?.let {
        repository.getRemoteMovies(genreId).applyIf(!repository.hasLocalResults) {
            saveResult(this)
        }
    } ?: emptyResult()

    private suspend fun saveResult(result: SResult<List<MovieEntity>>) {
        if (result is SResult.Success && result.data.isNotEmpty()) {
            repository.saveMovies(result.data)
        }
    }
}