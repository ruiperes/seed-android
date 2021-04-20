package io.ruiperes.seed.core.extension

fun Boolean?.isNullOrFalse(): Boolean {
    return this == null || this == false
}

fun Boolean?.ifNullAsFalse(): Boolean {
    return this ?: false
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun Any?.isNullOrEmpty(): Boolean {
    return this == null || (this is String && this.isEmpty())
}

fun Map<String, String>.filterEmptyValues() = this.filter { it.value.isNotEmpty() }
fun HashMap<String, Any>.filterEmptyValues() = this.filter { !it.value.isNullOrEmpty() }

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Enum<T>> T.toInt(): Int = this.ordinal

inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]
