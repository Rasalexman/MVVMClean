package com.mincor.mvvmclean.domain.usecases.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.repository.MoviesRepository

class GetMovieDetailUseCase(
    private val moviesRepository: MoviesRepository
) : ((Int)->LiveData<SResult<MovieEntity>>){
    override fun invoke(movieId: Int): LiveData<SResult<MovieEntity>> {
        return liveData {
            emitSource(
                moviesRepository.getLocalMovieById(movieId).map { localMovie ->
                    if(localMovie?.hasDetails == true) successResult(localMovie)
                    else emptyResult()
                }
            )
            val remoteResult = moviesRepository.getRemoteMovieById(movieId)
            if(remoteResult is SResult.Success) moviesRepository.saveMovie(remoteResult.data.apply { hasDetails = true })
            else emit(remoteResult)
        }
    }
}