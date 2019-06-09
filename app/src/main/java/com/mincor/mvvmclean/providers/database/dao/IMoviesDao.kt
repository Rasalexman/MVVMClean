package com.mincor.mvvmclean.providers.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.providers.database.dao.base.IBaseDao

@Dao
interface IMoviesDao : IBaseDao<MovieEntity> {

    @Query("SELECT * FROM MovieEntity WHERE genreIds LIKE '%' || :genreId  || '%' ORDER BY releaseDate DESC LIMIT 20")
    suspend fun getAll(genreId: Int): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE id = :movieId LIMIT 1")
    fun getById(movieId: Int): LiveData<MovieEntity?>
}