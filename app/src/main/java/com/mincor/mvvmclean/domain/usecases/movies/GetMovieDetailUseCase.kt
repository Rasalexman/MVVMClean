package com.mincor.mvvmclean.domain.usecases.movies

import androidx.lifecycle.LiveData
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository

class GetMovieDetailUseCase(
    private val moviesRepository: MoviesRepository
) : ((Int)->LiveData<SResult<MovieEntity>>){
    override fun invoke(movieId: Int): LiveData<SResult<MovieEntity>> {
        return moviesRepository.getMovieDetails(movieId)
    }
}