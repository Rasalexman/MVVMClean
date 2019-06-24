package com.mincor.mvvmclean.domain.repository

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.datasource.local.IGenresLocalDataSource
import com.mincor.mvvmclean.datasource.remote.IGenresRemoteDataSource
import com.mincor.mvvmclean.domain.model.local.GenreEntity

class GenresRepository(
    private val remoteDataSource: IGenresRemoteDataSource,
    private val localDataSource: IGenresLocalDataSource

) {
    suspend fun getGenresList(): SResult<List<GenreEntity>> {
        val localList = localDataSource.getGenresList()
        return if (localList.isNotEmpty()) {
            successResult(localList)
        } else {
            updateGenres()
        }
    }

    private suspend fun updateGenres() =
        remoteDataSource
            .getRemoteGenresList()
            .mapListTo()
            .also {
                if (it is SResult.Success) {
                    localDataSource.insertGenres(it.data)
                }
            }
}