package com.mincor.mvvmclean.datasource.remote

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.remote.GenreModel

interface IGenresRemoteDataSource {
    suspend fun getRemoteGenresList(): SResult<List<GenreModel>>
}