package com.mincor.mvvmclean.providers.network.api

import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.domain.model.remote.MovieModel
import com.mincor.mvvmclean.providers.network.responses.GetGenresResponse
import com.mincor.mvvmclean.providers.network.responses.GetMoviesByGenreIdResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieApi {

    /**
     * Get the list of official genres for movies.
     */
    @GET("genre/movie/list")
    fun getGenresList(): Call<GetGenresResponse>

    @GET("discover/movie")
    fun getMoviesListByGenreId(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("release_date.lte") lte: String = Consts.currentDate
    ): Call<GetMoviesByGenreIdResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int): Call<MovieModel>
}