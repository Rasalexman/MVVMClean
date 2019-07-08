package com.mincor.mvvmclean.domain.model.base

interface IConvertableTo<T> {
    fun convertTo(): T?
}