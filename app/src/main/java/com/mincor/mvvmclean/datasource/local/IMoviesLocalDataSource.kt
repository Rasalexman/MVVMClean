package com.mincor.mvvmclean.datasource.local

import androidx.lifecycle.LiveData
import com.mincor.mvvmclean.domain.model.local.MovieEntity

interface IMoviesLocalDataSource {
    suspend fun getAll(genreId: Int): List<MovieEntity>

    suspend fun getById(movieId: Int): LiveData<MovieEntity?>

    suspend fun saveMovies(data: List<MovieEntity>)

    suspend fun insertMovie(data: MovieEntity)
}