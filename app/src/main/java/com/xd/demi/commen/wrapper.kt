package com.xd.demi.commen

import com.xd.demi.bean.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.json.JSONObject

class ApiException(msg: String?) : Exception(msg)

fun Throwable.isApiExcetion(): Boolean = (this is ApiException)

// -------------------------------

// { RxJavaPlugins.onError(OnErrorNotImplementedException(it)) }
private val onErrorStub: (Throwable) -> Unit = { }
private val onCompleteStub: () -> Unit = {}
private val onNextStub: (Any) -> Unit = {}

fun <T : Any> Flowable<T>.subscribeBy(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub,
        onNext: (T) -> Unit = onNextStub
): Disposable = subscribe(onNext, onError, onComplete)

fun <T : Any> Observable<T>.subscribeBy(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub,
        onNext: (T) -> Unit = onNextStub
): Disposable = subscribe(onNext, onError, onComplete)

fun <INFO_TYPE, RESP_TYPE: HttpResp<INFO_TYPE>> onNextAdapter(onNext: (INFO_TYPE) -> Unit): (RESP_TYPE) -> Unit = {
    onNext.invoke(it.data)
}

fun <T : Any> Flowable<HttpResp<T>>.subscribeResp(
        onNext: (T) -> Unit = onNextStub,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
): Disposable = filterApiError().subscribe(onNextAdapter(onNext), onError, onComplete)

fun <T : Any> Flowable<HttpListResp<T>>.subscribeListResp(
        onNext: (List<T>) -> Unit = onNextStub,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
): Disposable = filterApiError().subscribe(onNextAdapter(onNext), onError, onComplete)

fun <T : Any> Flowable<HttpListInfoResp<T>>.subscribeListInfoResp(
        onNext: (ListInfo<T>) -> Unit = onNextStub,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
): Disposable = filterApiError().subscribe(onNextAdapter(onNext), onError, onComplete)

/**
 * 过滤api error
 */
fun <T : Any> Flowable<T>.filterApiError() = this.doOnNext {
    //if (it is HttpResp<*> && GBQConstants.RET_OK != it.ret) throw ApiException(it.msg)
    if (it is HttpResp<*> && 0 > it.errorCode) throw ApiException(it.errorMsg)
    if (it is JSONObject && it.optInt("errorCode", -1) != 0) throw ApiException(it.optString("errorMsg",""))
}
//
//fun <T> Flowable<T>.bindUntilEvent(owner: Any?, event: Lifecycle.Event): Flowable<T> {
//    return if (owner != null && owner is LifecycleOwner) {
//        compose(AndroidLifecycle.createLifecycleProvider(owner).bindUntilEvent(event))
//    } else {
//        this
//    }
//}