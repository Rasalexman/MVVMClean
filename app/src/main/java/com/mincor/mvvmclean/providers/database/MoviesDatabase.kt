package com.mincor.mvvmclean.providers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mincor.mvvmclean.domain.model.local.GenreEntity
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.providers.database.converters.FromListOfIntToStringConverter
import com.mincor.mvvmclean.providers.database.dao.IGenresDao
import com.mincor.mvvmclean.providers.database.dao.IMoviesDao

@Database(entities = [
    GenreEntity::class,
    MovieEntity::class
], version = 1)
@TypeConverters(FromListOfIntToStringConverter::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun getGenresDao(): IGenresDao
    abstract fun getMoviesDao(): IMoviesDao
}