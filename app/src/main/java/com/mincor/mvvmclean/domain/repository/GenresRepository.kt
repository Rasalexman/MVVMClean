package com.mincor.mvvmclean.domain.repository

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.datasource.local.IGenresLocalDataSource
import com.mincor.mvvmclean.datasource.remote.IGenresRemoteDataSource
import com.mincor.mvvmclean.domain.model.local.GenreEntity
import com.mincor.mvvmclean.domain.model.remote.GenreModel

class GenresRepository(
    private val remoteDataSource: IGenresRemoteDataSource,
    private val localDataSource: IGenresLocalDataSource

) {
    suspend fun getLocalGenreList(): SResult.Success<List<GenreEntity>> =
        localDataSource.getGenresList()

    suspend fun saveGenres(genresList: List<GenreEntity>) =
        localDataSource.insertGenres(genresList)

    suspend fun getRemoteGenresList(): SResult<List<GenreModel>> =
        remoteDataSource.getRemoteGenresList()
}