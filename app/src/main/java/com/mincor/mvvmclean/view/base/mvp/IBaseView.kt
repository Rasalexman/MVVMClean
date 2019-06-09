package com.mincor.room.presentation.base.mvp

/**
 * Created by a.minkin on 25.10.2017.
 */
interface IBaseView<out T : IBasePresenter<*>> {
    val presenter: T
    fun showError(error: String?)
}