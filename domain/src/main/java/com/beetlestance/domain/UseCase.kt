/*
 * Copyright 2020 BeetleStance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beetlestance.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * To be used for performing work without any result for the work
 *
 * [executeSync] performs the work synchronously
 * [invoke] returns flowable which will auto cancel once the block is complete
 */
abstract class UseCase<in P> {
    operator fun invoke(params: P): Flow<Status> {
        return flow {
            emit(Status.Started)
            doWork(params)
            emit(Status.Success)
        }.catch { throwable ->
            emit(Status.Error(throwable))
            Timber.e(throwable)
        }
    }

    // Will only be used when we know the result will be returned without errors
    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)
}

sealed class Status {
    object Started : Status()
    object Success : Status()
    data class Error(val t: Throwable) : Status()
}

suspend fun Flow<Status>.watchStatus(
    onStarted: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onError: (e: Throwable) -> Unit = {}
) = collect {
    when (it) {
        is Status.Started -> {
            onStarted()
        }
        is Status.Success -> {
            onSuccess()
        }
        is Status.Error -> {
            onError(it.t)
        }
    }
}

/**
 * To be used to perform work with a result type [R]
 *
 * This use case can be used for both synchronous and asynchronous call backs
 * ```
 * for synchronous, use terminal operators which returns result:
 * resultUseCase.invoke(input).first()
 *
 * for asynchronous
 * resultUseCase.invoke(input).collect{
 *      # do work
 * }
 * ```
 */
abstract class ResultUseCase<in P, out R> {
    operator fun invoke(params: P): Flow<R> {
        return flow {
            emit(doWork(params))
        }.catch { throwable ->
            Timber.e(throwable)
        }
    }

    protected abstract suspend fun doWork(params: P): R
}

/**
 * To be used for observing values from database or any source
 *
 * ```
 * listen to observable
 * observeUseCase.observe().collect{
 *      # do work
 * }
 *
 * set the parameters for creating observable
 * observeUseCase.invoke(input)
 *
 * ```
 */
abstract class ObserveUseCase<P : Any, T> {
    private val paramState = MutableStateFlow<P?>(null)

    operator fun invoke(params: P) {
        paramState.value = params
    }

    protected abstract fun createObservable(params: P): Flow<T>

    fun observe(): Flow<T> = paramState.filterNotNull().flatMapLatest {
        createObservable(it).catch { throwable ->
            Timber.e(throwable)
        }
    }
}

/**
 * To be used for work that are suspendable and has to be excecuted multiple times with different params
 * This is like result use case, but observe will be called once and work can be executed multiple times
 *
 * Does not take flowable in [createObservable] but creates one
 */
abstract class SuspendableWorkUseCase<P : Any, T> : ObserveUseCase<P, T>() {
    override fun createObservable(params: P): Flow<T> = flow {
        emit(doWork(params))
    }.catch { throwable ->
        Timber.e(throwable)
    }

    abstract suspend fun doWork(params: P): T
}


operator fun UseCase<Unit>.invoke() = invoke(Unit)
operator fun <T> ResultUseCase<Unit, T>.invoke() = invoke(Unit)
operator fun <T> ObserveUseCase<Unit, T>.invoke() = invoke(Unit)
