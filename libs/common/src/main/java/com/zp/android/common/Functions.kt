package com.zp.android.common

/**
 * Created by zhaopan on 2019/1/8.
 */


typealias Supplier<T> = () -> T

interface Consumer<T> {

    fun accept(t: T)
}