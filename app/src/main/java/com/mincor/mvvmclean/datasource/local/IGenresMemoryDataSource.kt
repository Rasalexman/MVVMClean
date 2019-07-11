package com.mincor.mvvmclean.datasource.local

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.GenreEntity

interface IGenresMemoryDataSource {
    suspend fun putGenresIntoMemoryCache(genresList: List<GenreEntity>)
    suspend fun getGenresFromMemoryCache(): SResult<List<GenreEntity>>
}