package com.mincor.mvvmclean.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.loading
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.domain.usecases.genres.GetGenresUseCase
import com.mincor.mvvmclean.viewmodel.uimodel.genres.GenreUI
import kotlinx.coroutines.Dispatchers

class GenresViewModel(
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    val dataList: LiveData<out SResult<List<GenreUI>>> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emit(getGenresUseCase.execute().mapListTo())
        }
}
