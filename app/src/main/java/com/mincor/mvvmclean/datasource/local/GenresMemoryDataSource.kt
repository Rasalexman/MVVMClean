package com.mincor.mvvmclean.datasource.local

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.domain.model.local.GenreEntity

class GenresMemoryDataSource : IGenresMemoryDataSource {

    private val memoryGenresList = mutableListOf<GenreEntity>()

    override suspend fun putGenresIntoMemoryCache(genresList: List<GenreEntity>) {
        if(memoryGenresList.isEmpty()) memoryGenresList.addAll(genresList)
    }

    override suspend fun getGenresFromMemoryCache(): SResult<List<GenreEntity>> {
        return successResult(memoryGenresList)
    }
}