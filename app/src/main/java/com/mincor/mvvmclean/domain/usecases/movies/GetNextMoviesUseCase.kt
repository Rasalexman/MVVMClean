package com.mincor.mvvmclean.domain.usecases.movies

import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.domain.repository.MoviesRepository

class GetNextMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend fun execute(genreId: Int?) = genreId?.let {
        repository.loadNextMovies(genreId)
    } ?: emptyResult()
}