package com.kinmin.core.common

/** Uniform result wrapper used across every layer. */
sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String, val cause: Throwable? = null) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}

inline fun <T> safeCall(block: () -> T): Resource<T> =
    try { Resource.Success(block()) }
    catch (e: Exception) { Resource.Error(e.message ?: "Unknown error", e) }
