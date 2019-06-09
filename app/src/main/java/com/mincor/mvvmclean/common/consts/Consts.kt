package com.mincor.mvvmclean.common.consts

import java.text.SimpleDateFormat
import java.util.*

object Consts {

    /// TEXT SIZES
    const val TEXT_SIZE_SMALL = 12f
    const val TEXT_SIZE_MEDIUM = 14f
    const val TEXT_SIZE_LARGE = 18f
    const val TEXT_SIZE_XLARGE = 22f

    val UI_DATE_FORMATTER
        get() = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val MODEL_DATE_FORMATTER
            get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val currentDate: String
        get() = MODEL_DATE_FORMATTER.format(Date()).toString()

    // SERVER URL
    const val SERVER_URL = "https://api.themoviedb.org/3/"

    // IMAGES URL require: size : w185
    const val IMAGES_URL = "https://image.tmdb.org/t/p/w500"
    const val IMAGES_BACKDROP_URL = "https://image.tmdb.org/t/p/original"

    // PAGES CONST
    const val PAGES_DEFAULT_MAX_COUNT = 1000
}