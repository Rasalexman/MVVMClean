package com.mincor.mvvmclean.domain.model.remote

import com.mincor.mvvmclean.domain.model.base.IConvertableTo
import com.mincor.mvvmclean.domain.model.local.GenreEntity

data class GenreModel(
    val id: Int?,
    val name: String?
) : IConvertableTo<GenreEntity> {
    override fun convertTo() = GenreEntity(this.id?:0, this.name.orEmpty())
}