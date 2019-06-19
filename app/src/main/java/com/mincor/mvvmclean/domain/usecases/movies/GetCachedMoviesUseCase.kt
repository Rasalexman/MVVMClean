package com.mincor.mvvmclean.domain.usecases.movies

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository

class GetCachedMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend fun execute(genreId: Int): SResult<List<MovieEntity>> {
        return repository.getLocalMovies(genreId).let { localList ->
            if (repository.hasLocalResults) successResult(localList)
            else emptyResult()
        }
    }
}