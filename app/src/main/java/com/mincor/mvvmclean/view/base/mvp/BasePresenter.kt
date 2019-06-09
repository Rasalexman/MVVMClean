package com.mincor.room.presentation.base.mvp

import com.rasalexman.coroutinesmanager.CoroutinesManager

abstract class BasePresenter<T> : CoroutinesManager(), IBasePresenter<T> {
    override var view: T? = null
    override fun onViewAttached() {}
    override fun onViewDestroyed() {}
    override fun onViewDetached() {}
}