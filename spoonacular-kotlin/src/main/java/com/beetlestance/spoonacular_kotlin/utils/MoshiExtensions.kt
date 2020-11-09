package com.beetlestance.spoonacular_kotlin.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

inline fun <reified T, reified R> T.serializedCopy(): R? {
    val sourceJson = MoshiSerializer.moshi.toJson(this)
    return MoshiSerializer.moshi.fromJson(sourceJson)
}

inline fun <reified T> Moshi.adapter(): JsonAdapter<T> {
    val type = object : TypeToken<T>() {}.type
    val adapterType = Types.newParameterizedType(T::class.java, type)
    val adapter: JsonAdapter<T> = adapter(adapterType)
    return adapter.nullSafe()
}

inline fun <reified T> Moshi.toJson(source: T): String {
    val adapter: JsonAdapter<T> = adapter()
    return adapter.toJson(source)
}

inline fun <reified T> Moshi.fromJson(source: String): T? {
    val adapter: JsonAdapter<T> = adapter()
    return adapter.fromJson(source)
}
