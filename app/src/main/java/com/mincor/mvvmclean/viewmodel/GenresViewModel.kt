package com.mincor.mvvmclean.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.loading
import com.mincor.mvvmclean.domain.usecases.genres.GetGenresUseCase
import com.mincor.mvvmclean.view.uimodels.genres.GenreUI
import kotlinx.coroutines.Dispatchers

class GenresViewModel(
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private val dataList: LiveData<SResult<List<GenreUI>>> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emit(getGenresUseCase.execute())
        }

    fun getDataList() = dataList
}
