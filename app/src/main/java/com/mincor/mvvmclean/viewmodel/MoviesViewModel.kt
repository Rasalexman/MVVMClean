package com.mincor.mvvmclean.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.loading
import com.mincor.mvvmclean.domain.usecases.movies.GetMoviesUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetNewMoviesUseCase
import com.mincor.mvvmclean.domain.usecases.movies.GetRemoteMoviesUseCase
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MoviesViewModel(appContext: Context) : ViewModel(), KodeinAware {

    override val kodein: Kodein by kodein(appContext)

    private val getNewMoviesUseCase: GetNewMoviesUseCase by instance()
    private val getNextMoviesUseCase: GetRemoteMoviesUseCase by instance()
    private val getCachedMoviesUseCase: GetMoviesUseCase by instance()

    private val newPagegenreId by lazy { MutableLiveData<Int>() }
    private val genreLiveId by lazy { MutableLiveData<Int>() }
    private val movieList by lazy { MutableLiveData<SResult<List<MovieUI>>>() }

    private var isLocalDataFetched = false

    private val newPageLiveData: LiveData<SResult<List<MovieUI>>> by lazy {
        newPagegenreId.switchMap { genreId ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(loading())
                emit(getNewMoviesUseCase.execute(genreId))
            }
        }
    }

    private val startPageLiveData: LiveData<SResult<List<MovieUI>>> by lazy {
        genreLiveId.switchMap { genreId ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(loading())
                emitSource(getCachedMoviesUseCase(genreId))
            }
        }
    }

    private val nextPageLiveData: LiveData<SResult<List<MovieUI>>> by lazy {
        movieList.switchMap {
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(loading())
                emit(getNextMoviesUseCase.execute(genreLiveId.value))
            }
        }
    }

    fun getNewPage(): LiveData<SResult<List<MovieUI>>> = newPageLiveData
    fun getStartPage(): LiveData<SResult<List<MovieUI>>> = startPageLiveData
    fun getNextPage(): LiveData<SResult<List<MovieUI>>> = nextPageLiveData

    fun setGenreId(genreId: Int) {
        isLocalDataFetched = false
        genreLiveId.value = genreId
    }

    fun loadNext() {
        movieList.postValue(emptyResult())
    }

    fun loadNew(genreId: Int) {
        newPagegenreId.value = genreId
    }
}
