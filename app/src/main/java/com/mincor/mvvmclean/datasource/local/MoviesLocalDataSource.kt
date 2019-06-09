package com.mincor.mvvmclean.datasource.local

import androidx.lifecycle.LiveData
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.providers.database.dao.IMoviesDao

class MoviesLocalDataSource(
    private val moviesDao: IMoviesDao
) : IMoviesLocalDataSource {

    override suspend fun getAll(genreId: Int): List<MovieEntity> {
        return moviesDao.getAll(genreId)
    }

    override suspend fun getById(movieId: Int): LiveData<MovieEntity?> {
        return moviesDao.getById(movieId)
    }

    override suspend fun saveMovies(data: List<MovieEntity>) {
        moviesDao.insertAll(data)
    }

    override suspend fun insertMovie(data: MovieEntity) {
        moviesDao.insert(data)
    }
}