package com.mincor.mvvmclean.domain.usecases.genres

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.domain.model.local.GenreEntity
import com.mincor.mvvmclean.domain.repository.GenresRepository

class GetLocalGenresUseCase(
    private val repository: GenresRepository
) {
    suspend fun execute(): SResult<List<GenreEntity>> =
        repository.getLocalGenreList()
}