package com.mincor.mvvmclean.domain.usecases.movies

import androidx.lifecycle.LiveData
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository

class GetMoviesUseCase(
    private val repository: MoviesRepository
) : ((Int) -> LiveData<SResult<List<MovieEntity>>>){

    override fun invoke(genreId: Int): LiveData<SResult<List<MovieEntity>>> {
        return repository.getMoviesList(genreId)
    }
}