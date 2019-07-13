package com.mincor.mvvmclean.datasource.local

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.GenreEntity

interface IGenresCacheDataSource {
    suspend fun putGenresInCache(genresList: List<GenreEntity>)
    suspend fun getGenresFromCache(): SResult.Success<List<GenreEntity>>
}