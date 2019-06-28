package com.mincor.mvvmclean.domain.repository

import androidx.lifecycle.LiveData
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.common.dto.mapTo
import com.mincor.mvvmclean.datasource.local.IMoviesLocalDataSource
import com.mincor.mvvmclean.datasource.remote.IMoviesRemoteDataSource
import com.mincor.mvvmclean.domain.model.local.MovieEntity

class MoviesRepository(
    private val localDataSource: IMoviesLocalDataSource,
    private val remoteDataSource: IMoviesRemoteDataSource
) {

    var hasLocalResults = false
        private set

    suspend fun getLocalMovies(genreId: Int): SResult<List<MovieEntity>> =
        localDataSource
            .getAll(genreId)
            .apply {
                hasLocalResults = this.data.isNotEmpty()
            }

    suspend fun getRemoteMovies(genreId: Int): SResult<List<MovieEntity>> {
        return remoteDataSource
            .getByGenreId(genreId)
            .mapListTo()
    }

    suspend fun saveMovies(data: List<MovieEntity>) {
        hasLocalResults = data.isNotEmpty()
        localDataSource.insertAll(data)
    }

    suspend fun saveMovie(data: MovieEntity) = localDataSource.insert(data)

    suspend fun getLocalMovieById(movieId: Int): LiveData<SResult<MovieEntity>> =
        localDataSource.getById(movieId)

    suspend fun getRemoteMovieById(movieId: Int): SResult<MovieEntity> =
        remoteDataSource.getMovieDetails(movieId).mapTo()

    fun clear() {
        hasLocalResults = false
        remoteDataSource.clearPage()
    }
}