package com.mincor.mvvmclean.domain.usecases.genres

import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.dto.mapListTo
import com.mincor.mvvmclean.view.uimodels.genres.GenreUI

class GetGenresUseCase(
    private val getLocalGenresUseCase: GetLocalGenresUseCase,
    private val getRemoteGenresUseCase: GetRemoteGenresUseCase
) {
    suspend fun execute(): SResult<List<GenreUI>> {
        return getLocalGenresUseCase.execute().let { localResultList ->
            if(localResultList is SResult.Success && localResultList.data.isNotEmpty()) localResultList
            else getRemoteGenresUseCase.execute()
        }.mapListTo()
    }
}