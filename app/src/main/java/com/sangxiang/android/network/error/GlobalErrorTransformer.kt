package com.sangxiang.android.network.error
import io.reactivex.*

typealias OnNextInterceptor<T> = (T) -> Observable<T>
typealias OnErrorResumeNext<T> = (Throwable) -> Observable<T>
typealias OnErrorConsumer = (Throwable) -> Unit

class GlobalErrorTransformer<T> constructor(
        private val onNextInterceptor: OnNextInterceptor<T> = { Observable.just(it) },
        private val onErrorResumeNext: OnErrorResumeNext<T> = { Observable.error(it) },
        private val onErrorConsumer: OnErrorConsumer = { }
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>,
        MaybeTransformer<T, T>, CompletableTransformer {

    override fun apply(upstream: Observable<T>): Observable<T> =
            upstream
                    .flatMap {
                        onNextInterceptor(it)
                    }
                    .onErrorResumeNext { throwable: Throwable ->
                        onErrorResumeNext(throwable)
                    }
                    .doOnError(onErrorConsumer)

    override fun apply(upstream: Completable): Completable =
            upstream
                    .onErrorResumeNext {
                        onErrorResumeNext(it).ignoreElements()
                    }
                    .doOnError(onErrorConsumer)

    override fun apply(upstream: Flowable<T>): Flowable<T> =
            upstream
                    .flatMap {
                        onNextInterceptor(it)
                                .toFlowable(BackpressureStrategy.BUFFER)
                    }
                    .onErrorResumeNext { throwable: Throwable ->
                        onErrorResumeNext(throwable)
                                .toFlowable(BackpressureStrategy.BUFFER)
                    }
                    .doOnError(onErrorConsumer)

    override fun apply(upstream: Maybe<T>): Maybe<T> =
            upstream
                    .flatMap {
                        onNextInterceptor(it)
                                .firstElement()
                    }
                    .onErrorResumeNext { throwable: Throwable ->
                        onErrorResumeNext(throwable)
                                .firstElement()
                    }
                    .doOnError(onErrorConsumer)

    override fun apply(upstream: Single<T>): Single<T> =
            upstream
                    .flatMap {
                        onNextInterceptor(it)
                                .firstOrError()
                    }
                    .onErrorResumeNext { throwable ->
                        onErrorResumeNext(throwable)
                                .firstOrError()
                    }
                    .doOnError(onErrorConsumer)
}