package com.zp.android.base

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by zhaopan on 2018/6/23.
 */
object RxBus{
    // toSerialized method made bus thread safe
    private var mBus: Subject<Any> = PublishSubject.create<Any>().toSerialized()
    private var mStickyEventMap: MutableMap<Class<*>, Any>  = ConcurrentHashMap()

    fun get(): RxBus = this
    /**
     * 发送事件
     */
    fun post(event: Any) {
        mBus.onNext(event)//
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }

    /**
     * 判断是否有订阅者
     */
    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    /**
     * Stciky 相关
     */

    /**
     * 发送一个新Sticky事件
     */
    fun postSticky(event: Any) {
        synchronized(mStickyEventMap) {
            mStickyEventMap.put(event.javaClass, event)
        }
        post(event)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> toObservableSticky(eventType: Class<T>): Observable<T> {
        synchronized(mStickyEventMap) {
            val observable = mBus.ofType(eventType)
            val event = mStickyEventMap[eventType]

            return if (event != null) {
                observable.mergeWith(Observable.just(eventType.cast(event)))
            } else {
                observable
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    fun <T> getStickyEvent(eventType: Class<T>): T? {
        synchronized(mStickyEventMap) {
            return eventType.cast(mStickyEventMap[eventType])
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    fun <T> removeStickyEvent(eventType: Class<T>): T? {
        synchronized(mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType))
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllStickyEvents() {
        synchronized(mStickyEventMap) {
            mStickyEventMap.clear()
        }
    }

    /**
     * 普通订阅解绑
     * @param disposable
     */
    fun rxBusUnbund(disposable: CompositeDisposable?) {
        if (null != disposable && !disposable.isDisposed) {
            disposable.clear()
        }
    }
}