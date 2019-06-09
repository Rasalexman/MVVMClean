package com.mincor.room.presentation.base.mvp

/**
 * Created by a.minkin on 25.10.2017.
 */
interface IBasePresenter<T> {
    var view: T?

    fun onDestroyView() {
        view = null
        onViewDestroyed()
    }

    fun onViewCreated(viewInstance: Any?) {
        view = viewInstance as? T
    }

    fun onViewAttached()
    fun onViewDetached()
    fun onViewDestroyed()
}