package com.mincor.mvvmclean.datasource.local

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.GenreEntity

interface IGenresLocalDataSource {
    suspend fun getGenresList(): SResult<List<GenreEntity>>
    suspend fun insertGenres(data: List<GenreEntity>)
}