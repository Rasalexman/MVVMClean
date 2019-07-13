package com.mincor.mvvmclean.datasource.local

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.domain.model.local.GenreEntity

class GenresCacheDataSource : IGenresCacheDataSource {

    private val memoryGenresList = mutableListOf<GenreEntity>()

    override suspend fun putGenresInCache(genresList: List<GenreEntity>) {
        if(memoryGenresList.isEmpty()) memoryGenresList.addAll(genresList)
    }

    override suspend fun getGenresFromCache(): SResult.Success<List<GenreEntity>> {
        return successResult(memoryGenresList)
    }
}