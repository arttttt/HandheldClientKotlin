package com.arttttt.hendheldclient.utils

fun <K, V, R : Any> Map<K, V>.mapValuesNutNull(
    transform: (Map.Entry<K, V>) -> R?,
): Map<K, R> {
    return buildMap {
        this@mapValuesNutNull.forEach { entry ->
            val transformed = transform(entry)

            if (transformed != null) {
                put(entry.key, transformed)
            }
        }
    }
}