package com.vf.pokemontest.core.network

import kotlin.coroutines.cancellation.CancellationException

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val throwable: Throwable) : NetworkResult<Nothing>
}

/**
 * Wraps a suspend call into a [NetworkResult].
 *
 * IMPORTANT: [CancellationException] is rethrown — swallowing it would break
 * structured concurrency (a cancelled coroutine could appear to "succeed" with an error,
 * and parent cancellation chains stop working).
 */
suspend inline fun <T> safeApiCall(crossinline call: suspend () -> T): NetworkResult<T> =
    try {
        NetworkResult.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        NetworkResult.Error(e)
    }