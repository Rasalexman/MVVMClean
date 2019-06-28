package com.mincor.mvvmclean.domain.usecases.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.repository.MoviesRepository
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI

class GetMoviesUseCase(
    private val repository: MoviesRepository,
    private val getCachedMoviesUseCase: GetCachedMoviesUseCase,
    private val getRemoteMoviesUseCase: GetRemoteMoviesUseCase
) : ((Int) -> LiveData<SResult<List<MovieUI>>>) {

    override fun invoke(genreId: Int): LiveData<SResult<List<MovieUI>>> {
        return liveData {
            repository.clear()
            emit(getCachedMoviesUseCase.execute(genreId))
            emit(getRemoteMoviesUseCase.execute(genreId))
        }
    }
}