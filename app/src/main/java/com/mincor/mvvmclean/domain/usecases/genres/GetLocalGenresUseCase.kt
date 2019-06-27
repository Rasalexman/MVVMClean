package com.mincor.mvvmclean.domain.usecases.genres

import com.mincor.mvvmclean.domain.repository.GenresRepository

class GetLocalGenresUseCase(
    private val repository: GenresRepository
) {
    suspend fun execute() =
        repository.getLocalGenreList()
}