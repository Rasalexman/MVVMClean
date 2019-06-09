package com.mincor.mvvmclean.datasource.remote

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.errorResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.common.utils.log
import com.mincor.mvvmclean.domain.model.remote.GenreModel
import com.mincor.mvvmclean.providers.network.api.IMovieApi
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class GenresRemoteDataSource(
    private val moviesApi: IMovieApi
) : IGenresRemoteDataSource {

    override suspend fun getRemoteGenresList(): SResult<List<GenreModel>> =
        when (val result = moviesApi.getGenresList().awaitResult()) {
            is Result.Ok -> {
                log { "HERE IS A RESULT OK ${Thread.currentThread()}" }
                successResult(result.value.genres)
            }
            is Result.Error -> {
                log { "THERE IS AN ERROR" }
                errorResult(result.response.code(), result.exception.message())
            }
            is Result.Exception -> {
                log { "THERE IS AN EXCEPTION" }
                emptyResult()
            }
        }
}