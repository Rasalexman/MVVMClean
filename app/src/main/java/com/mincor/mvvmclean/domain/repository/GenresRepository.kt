package com.mincor.mvvmclean.domain.repository

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.datasource.local.IGenresLocalDataSource
import com.mincor.mvvmclean.datasource.local.IGenresCacheDataSource
import com.mincor.mvvmclean.datasource.remote.IGenresRemoteDataSource
import com.mincor.mvvmclean.domain.model.local.GenreEntity

class GenresRepository(
    private val remoteDataSource: IGenresRemoteDataSource,
    private val localDataSource: IGenresLocalDataSource,
    private val memoryDataSource: IGenresCacheDataSource
) {
    suspend fun getLocalGenreList(): SResult<List<GenreEntity>> =
        memoryDataSource.getGenresFromCache().takeIf { it.data.isNotEmpty() }
            ?: localDataSource.getGenresList()

    suspend fun saveGenres(genresList: List<GenreEntity>) =
        localDataSource.insertGenres(genresList)
            .also { memoryDataSource.putGenresInCache(genresList) }

    suspend fun getRemoteGenresList(): SResult<List<GenreEntity>> =
        remoteDataSource.getRemoteGenresList().mapListTo()
}