package com.mincor.room.presentation.base.mvp

import com.rasalexman.coroutinesmanager.CoroutinesManager
import com.rasalexman.coroutinesmanager.ICoroutinesManager

abstract class BaseCoroutineManagerPresenter<T> constructor(coroutinesManager: CoroutinesManager) :
    IBasePresenter<T>, ICoroutinesManager by coroutinesManager {
    override var view: T? = null
    override fun onViewAttached() {}
    override fun onViewDestroyed() {}
    override fun onViewDetached() {}
}