package com.mincor.mvvmclean.datasource.local

import androidx.lifecycle.LiveData
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity

interface IMoviesLocalDataSource {
    suspend fun getAll(genreId: Int): SResult.Success<List<MovieEntity>>

    suspend fun getById(movieId: Int): LiveData<SResult<MovieEntity>>

    suspend fun insertAll(data: List<MovieEntity>)

    suspend fun insert(data: MovieEntity)
}