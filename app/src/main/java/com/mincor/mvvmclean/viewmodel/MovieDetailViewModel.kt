package com.mincor.mvvmclean.viewmodel

import androidx.lifecycle.*
import com.mincor.mvvmclean.common.dto.loading
import com.mincor.mvvmclean.domain.usecases.movies.GetMovieDetailUseCase
import kotlinx.coroutines.Dispatchers

class MovieDetailViewModel(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val movieLiveId: MutableLiveData<Int> = MutableLiveData()
    private val movieDetails = movieLiveId.switchMap { movieId ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emitSource(getMovieDetailUseCase(movieId))
        }
    }

    fun getMovieDetails() = movieDetails

    fun setMovieId(movieId: Int) {
        movieLiveId.value = movieId
    }

}