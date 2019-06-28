package com.mincor.mvvmclean.domain.usecases.genres

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.domain.repository.GenresRepository
import com.mincor.mvvmclean.view.uimodels.genres.GenreUI

class GetLocalGenresUseCase(
    private val repository: GenresRepository
) {
    suspend fun execute(): SResult<List<GenreUI>> =
        repository.getLocalGenreList().mapListTo()
}