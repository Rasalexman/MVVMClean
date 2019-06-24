package com.mincor.mvvmclean.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mincor.mvvmclean.domain.model.base.IConvertableTo
import com.mincor.mvvmclean.view.uimodels.genres.GenreUI

@Entity
data class GenreEntity(
    @PrimaryKey
    val id: Int,
    val name: String
) : IConvertableTo<GenreUI> {
    override fun convertTo() = GenreUI(this.id, this.name)
}