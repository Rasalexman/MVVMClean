package com.mincor.mvvmclean.domain.usecases.movies

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI

class GetCachedMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend fun execute(genreId: Int): SResult<List<MovieUI>> {
        return repository.getLocalMovies(genreId).let { localList ->
            if (repository.hasLocalResults) localList
            else emptyResult()
        }.mapListTo()
    }
}