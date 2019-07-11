package com.mincor.mvvmclean.datasource.local

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.domain.model.local.GenreEntity
import com.mincor.mvvmclean.providers.database.dao.IGenresDao

class GenresLocalDataSource(
    private val genresDao: IGenresDao
) : IGenresLocalDataSource, IGenresMemoryDataSource {

    private val memoryGenresList = mutableListOf<GenreEntity>()

    override suspend fun getGenresList(): SResult<List<GenreEntity>> = successResult(genresDao.getAll())
    override suspend fun insertGenres(data: List<GenreEntity>) = genresDao.insertAll(data)

    override suspend fun putGenresIntoMemoryCache(genresList: List<GenreEntity>) {
        memoryGenresList.addAll(genresList)
    }

    override suspend fun getGenresFromMemoryCache(): SResult<List<GenreEntity>> {
        return successResult(memoryGenresList)
    }
}