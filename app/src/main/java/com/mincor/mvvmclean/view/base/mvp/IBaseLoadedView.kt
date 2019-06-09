package com.mincor.room.presentation.base.mvp

import com.mikepenz.fastadapter.items.AbstractItem

interface IBaseLoadedView<out T : IBasePresenter<*>> : IBaseView<T> {
    /**
     * Показываем футер для списка
     */
    fun showLoading()
    fun hideLoading()

    fun showNewLoaded(list:List<AbstractItem<*, *>>)
    fun showLoaded(list:List<AbstractItem<*, *>>)
}