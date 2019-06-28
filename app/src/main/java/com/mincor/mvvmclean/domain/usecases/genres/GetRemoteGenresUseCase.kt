package com.mincor.mvvmclean.domain.usecases.genres

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.domain.model.local.GenreEntity
import com.mincor.mvvmclean.domain.repository.GenresRepository

class GetRemoteGenresUseCase(
    private val repository: GenresRepository
) {
    suspend fun execute(): SResult<List<GenreEntity>> =
        repository
            .getRemoteGenresList()
            .mapListTo()
            .also {
                saveResult(it)
            }

    private suspend fun saveResult(result: SResult<List<GenreEntity>>) {
        if (result is SResult.Success && result.data.isNotEmpty()) {
            repository.saveGenres(result.data)
        }
    }
}