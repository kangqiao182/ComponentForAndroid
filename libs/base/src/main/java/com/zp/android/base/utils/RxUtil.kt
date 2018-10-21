package com.zp.android.base.utils

import com.zp.android.net.function.RetryWithDelay
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by zhaopan on 2018/8/16.
 */

object RxUtil {

    /**
     * Applies standard Schedulers to an {@link Observable}, ie IO for subscription, Main Thread for
     * onNext/onComplete/onError
     */
    fun <T> applySchedulersToObservable(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Consumer<Throwable> { Timber.e(it) })
        }
    }

    /**
     * Applies standard Schedulers to a [Single], ie IO for subscription, Main Thread for
     * onNext/onComplete/onError
     */
    fun <T> applySchedulersToSingle(): SingleTransformer<T, T> {
        return SingleTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Consumer<Throwable> { Timber.e(it) })
        }
    }

    /**
     * Applies standard Schedulers to a [io.reactivex.Completable], ie IO for subscription,
     * Main Thread for onNext/onComplete/onError
     */
    fun applySchedulersToCompletable(): CompletableTransformer {
        return CompletableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Consumer<Throwable> { Timber.e(it) })
        }
    }

    /**
     * Allows you to call two different [Observable] objects based on result of a predicate.
     */
    fun <T, R> ternary(
        predicate: Function<T, Boolean>,
        ifTrue: Function<in T, out Observable<out R>>,
        ifFalse: Function<in T, out Observable<out R>>
    ): Function<in T, out Observable<out R>> {
        return Function { item ->
            if (predicate.apply(item))
                ifTrue.apply(item)
            else
                ifFalse.apply(item)
        }
    }

    /**
     * 延时delay秒后重复执行.
     */
    fun repeatWhenDelay(delay: Int): Function<Observable<Any>, ObservableSource<*>> {
        return Function { objectObservable ->
            objectObservable.delay(delay.toLong(), TimeUnit.SECONDS)
        }
    }

    fun <T> takeUntil(isFinished: Boolean): Predicate<T> {
        return Predicate {
            isFinished
        }
    }

    private class Wrapper(val index: Int, val throwable: Throwable)

    fun retryAndDelay(retry: Int = 3, delay: Int = 5000): Function<Observable<out Throwable>, ObservableSource<*>> {
        return Function {
            it.zipWith(Observable.range(1, retry + 1), BiFunction<Throwable, Int, Wrapper> { t1, t2 -> Wrapper(t2, t1) })
                .flatMap { wrapper ->
                    val t = wrapper.throwable
                    if ((t is ConnectException
                                || t is SocketTimeoutException
                                || t is TimeoutException
                                || t is HttpException)
                        && wrapper.index < retry + 1
                    ) {
                        Observable.timer((delay * wrapper.index).toLong(), TimeUnit.SECONDS)
                    } else Observable.error<Any>(wrapper.throwable)
                }
        }
    }

}