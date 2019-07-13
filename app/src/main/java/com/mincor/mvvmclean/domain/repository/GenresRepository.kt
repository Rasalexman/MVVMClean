package com.mincor.mvvmclean.domain.repository

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.datasource.local.IGenresLocalDataSource
import com.mincor.mvvmclean.datasource.local.IGenresMemoryDataSource
import com.mincor.mvvmclean.datasource.remote.IGenresRemoteDataSource
import com.mincor.mvvmclean.domain.model.local.GenreEntity

class GenresRepository(
    private val remoteDataSource: IGenresRemoteDataSource,
    private val localDataSource: IGenresLocalDataSource,
    private val memoryDataSource: IGenresMemoryDataSource
) {
    suspend fun getLocalGenreList(): SResult<List<GenreEntity>> =
        memoryDataSource.getGenresFromMemoryCache().takeIf { it.data.isNotEmpty() }
            ?: localDataSource.getGenresList()

    suspend fun saveGenres(genresList: List<GenreEntity>) =
        localDataSource.insertGenres(genresList)
            .also { memoryDataSource.putGenresIntoMemoryCache(genresList) }

    suspend fun getRemoteGenresList(): SResult<List<GenreEntity>> =
        remoteDataSource.getRemoteGenresList().mapListTo()
}