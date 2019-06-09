package com.mincor.mvvmclean.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mincor.mvvmclean.common.dto.*
import com.mincor.mvvmclean.common.utils.applyIf
import com.mincor.mvvmclean.datasource.local.IMoviesLocalDataSource
import com.mincor.mvvmclean.datasource.remote.IMoviesRemoteDataSource
import com.mincor.mvvmclean.domain.model.local.MovieEntity

class MoviesRepository(
    private val localDataSource: IMoviesLocalDataSource,
    private val remoteDataSource: IMoviesRemoteDataSource
) {

    private var hasLocalResults = false

    fun getMoviesList(genreId: Int): LiveData<SResult<List<MovieEntity>>> {
        return liveData {
            hasLocalResults = false
            remoteDataSource.clearPage()
            emit(getCachedMovies(genreId))
            emit(loadNextMovies(genreId))
        }
    }

    private suspend fun getCachedMovies(genreId: Int): SResult<List<MovieEntity>> =
         localDataSource.getAll(genreId).let { localList ->
            hasLocalResults = localList.isNotEmpty()
            if (hasLocalResults) successResult(localList)
            else emptyResult()
        }

    suspend fun loadNextMovies(genreId: Int): SResult<List<MovieEntity>> {
        return remoteDataSource
            .getByGenreId(genreId)
            .mapListTo()
            .applyIf(!hasLocalResults) {
                saveResult(this)
            }
    }

    fun getMovieDetails(movieId: Int): LiveData<SResult<MovieEntity>> =
        liveData {
            emitSource(
                localDataSource.getById(movieId).map { localMovie ->
                    if(localMovie?.hasDetails == true) successResult(localMovie)
                    else emptyResult()
                }
            )
            val remoteResult = remoteDataSource.getMovieDetails(movieId).mapTo()
            if(remoteResult is SResult.Success) localDataSource.insertMovie(remoteResult.data.apply { hasDetails = true })
            else emit(remoteResult)
        }

    private suspend fun saveResult(result: SResult<List<MovieEntity>>) {
        if (result is SResult.Success && result.data.isNotEmpty()) {
            hasLocalResults = true
            localDataSource.saveMovies(result.data)
        }
    }
}