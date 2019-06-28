package com.mincor.mvvmclean.datasource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.providers.database.dao.IMoviesDao

class MoviesLocalDataSource(
    private val moviesDao: IMoviesDao
) : IMoviesLocalDataSource {

    override suspend fun getAll(genreId: Int): SResult.Success<List<MovieEntity>> {
        return successResult(moviesDao.getAll(genreId))
    }

    override suspend fun getById(movieId: Int): LiveData<SResult<MovieEntity>> {
        return moviesDao.getById(movieId).map { localMovie ->
            if(localMovie?.hasDetails == true) successResult(localMovie)
            else emptyResult()
        }
    }

    override suspend fun insertAll(data: List<MovieEntity>) {
        moviesDao.insertAll(data)
    }

    override suspend fun insert(data: MovieEntity) {
        moviesDao.insert(data)
    }
}