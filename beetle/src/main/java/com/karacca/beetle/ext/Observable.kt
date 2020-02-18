package com.karacca.beetle.ext

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * @user: omerkaraca
 * @date: 2019-06-18
 */

fun <T> Observable<T>.sub(onNext: (T) -> Unit, onError: ((Throwable) -> Unit)? = null): Disposable =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError ?: { })