package com.mincor.mvvmclean.domain.usecases.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
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
        }.map { it.mapListTo() } // we can do the same thing by `.mapListTo()` with every execute function of useCase
    }
}