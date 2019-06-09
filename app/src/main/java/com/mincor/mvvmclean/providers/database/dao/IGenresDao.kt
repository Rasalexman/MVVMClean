package com.mincor.mvvmclean.providers.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mincor.mvvmclean.providers.database.dao.base.IBaseDao
import com.mincor.mvvmclean.domain.model.local.GenreEntity

@Dao
interface IGenresDao : IBaseDao<GenreEntity> {

    @Query("SELECT * FROM GenreEntity ORDER BY name ASC")
    suspend fun getAll(): List<GenreEntity>
}