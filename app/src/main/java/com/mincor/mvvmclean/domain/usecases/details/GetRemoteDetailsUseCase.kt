package com.mincor.mvvmclean.domain.usecases.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository

class GetRemoteDetailsUseCase(
    private val moviesRepository: MoviesRepository
) : ((Int) -> LiveData<SResult<MovieEntity>>) {
    override fun invoke(movieId: Int): LiveData<SResult<MovieEntity>> {
        return liveData {
            emit(moviesRepository.getRemoteMovieById(movieId).let { remoteResult ->
                if (remoteResult is SResult.Success) {
                    moviesRepository.saveMovie(remoteResult.data.apply {
                        hasDetails = true
                    })
                }
                remoteResult
            })
        }
    }
}