package com.mincor.mvvmclean.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.loading
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.domain.usecases.movies.GetMoviesUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetNextMoviesUseCase
import com.mincor.mvvmclean.viewmodel.uimodel.movies.MovieUI
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MoviesViewModel(appContext: Context) : ViewModel(), KodeinAware {

    override val kodein: Kodein by kodein(appContext)

    private val getNextUseCase: GetNextMoviesUseCase by instance()
    private val getCachedUseCase: GetMoviesUseCase by instance()

    private val genreLiveId: MutableLiveData<Int> = MutableLiveData()
    private val movieList: MutableLiveData<SResult<List<MovieUI>>> = MutableLiveData()

    private var isLocalDataFetched = false

    private val startPageLiveData: LiveData<SResult<List<MovieUI>>> = genreLiveId.switchMap { genreId ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            val result = getCachedUseCase(genreId).map { it.mapListTo() }
            emitSource(result)
        }
    }

    private val nextPageLiveData: LiveData<SResult<List<MovieUI>>> = movieList.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emit(getNextUseCase.execute(genreLiveId.value).mapListTo())
        }
    }

    fun getStartPage(): LiveData<SResult<List<MovieUI>>> = startPageLiveData
    fun getNextPage(): LiveData<SResult<List<MovieUI>>> = nextPageLiveData

    fun setGenreId(genreId: Int) {
        isLocalDataFetched = false
        genreLiveId.value = genreId
    }

    fun loadNext() {
        movieList.postValue(emptyResult())
    }
}
