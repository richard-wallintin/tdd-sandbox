package util

data class Memo<P, R>(val cache: MutableMap<P, R> = mutableMapOf()) {
    inline fun recall(parameter: P, compute: (P) -> R): R =
        cache[parameter] ?: compute(parameter).also { cache[parameter] = it }
}