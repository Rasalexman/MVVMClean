package com.mincor.mvvmclean.domain.usecases.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.domain.usecases.details.GetLocalDetailsUseCase
import com.mincor.mvvmclean.domain.usecases.details.GetRemoteDetailsUseCase

class GetMovieDetailUseCase(
    private val getLocalDetailsUseCase: GetLocalDetailsUseCase,
    private val getRemoteDetailsUseCase: GetRemoteDetailsUseCase
) : ((Int) -> LiveData<SResult<MovieEntity>>) {
    override fun invoke(movieId: Int): LiveData<SResult<MovieEntity>> {
        return getLocalDetailsUseCase(movieId).switchMap {
            if(it is SResult.Success) liveData<SResult<MovieEntity>> { emit(it) }
            else getRemoteDetailsUseCase(movieId)
        }
    }
}