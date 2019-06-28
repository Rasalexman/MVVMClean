package com.mincor.mvvmclean.domain.repository

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.datasource.local.IGenresLocalDataSource
import com.mincor.mvvmclean.datasource.remote.IGenresRemoteDataSource
import com.mincor.mvvmclean.domain.model.local.GenreEntity

class GenresRepository(
    private val remoteDataSource: IGenresRemoteDataSource,
    private val localDataSource: IGenresLocalDataSource
) {
    suspend fun getLocalGenreList(): SResult<List<GenreEntity>> =
        localDataSource.getGenresList()

    suspend fun saveGenres(genresList: List<GenreEntity>) =
        localDataSource.insertGenres(genresList)

    suspend fun getRemoteGenresList(): SResult<List<GenreEntity>> =
        remoteDataSource.getRemoteGenresList().mapListTo()
}