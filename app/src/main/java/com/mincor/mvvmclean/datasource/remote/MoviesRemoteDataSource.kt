package com.mincor.mvvmclean.datasource.remote

import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.emptyResult
import com.mincor.mvvmclean.common.dto.errorResult
import com.mincor.mvvmclean.common.dto.successResult
import com.mincor.mvvmclean.common.utils.log
import com.mincor.mvvmclean.domain.model.remote.MovieModel
import com.mincor.mvvmclean.providers.network.api.IMovieApi
import com.mincor.mvvmclean.providers.network.responses.GetMoviesByGenreIdResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class MoviesRemoteDataSource(
    private val moviesApi: IMovieApi
) : IMoviesRemoteDataSource {

    private var currentPage = 1
    private var maxPages = Consts.PAGES_DEFAULT_MAX_COUNT
    private val mutex = Mutex()

    private val isCanChangePage: Boolean
        get() = currentPage < maxPages

    override suspend fun getByGenreId(genreId: Int): SResult<List<MovieModel>> =
        if(isCanChangePage) {
            requestHandler(genreId = genreId, page = currentPage)
        } else emptyResult()

    private suspend fun requestHandler(genreId: Int, page: Int) = mutex.withLock {
        when (val result = moviesApi.getMoviesListByGenreId(genreId, page).awaitResult()) {
            is Result.Ok -> {
                log { "HERE IS A RESULT OK ${result.value}" }
                val resultValue = result.value
                changePage(resultValue)
                successResult(resultValue.results)
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


    override suspend fun getMovieDetails(movieId: Int): SResult<MovieModel> {
        return when(val result = moviesApi.getMovieDetails(movieId).awaitResult()) {
            is Result.Ok -> {
                log { "HERE IS A RESULT OK ${result.value}" }
                successResult(result.value)
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

    /**
     * Clear Paging
     */
    override fun clearPage() {
        currentPage = 1
        maxPages = Consts.PAGES_DEFAULT_MAX_COUNT
    }

    /**
     * Increase currentPage count if available
     *
     * @param response - current request response data
     */
    private fun changePage(response: GetMoviesByGenreIdResponse) {
        val currentPage = response.page
        maxPages = response.total_pages
        if(currentPage < maxPages) this.currentPage++
    }
}