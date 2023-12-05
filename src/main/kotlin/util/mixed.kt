package util

fun Int.power(exp: Int): Int = if (exp == 0) 1 else (this * this.power(exp - 1))
fun String.integers() =
    split(Regex("\\s+")).filter { it.isNotBlank() }.map { it.toInt() }

fun String.longs() =
    split(Regex("\\s+")).filter { it.isNotBlank() }.map { it.toLong() }

fun <T> Sequence<T>.chunkedBy(predicate: (T) -> Boolean) = sequence<List<T>> {
    val collect = mutableListOf<T>()
    this@chunkedBy.forEach {
        if (predicate(it)) {
            if (collect.isNotEmpty()) yield(collect.toList())
            collect.clear()
        } else collect.add(it)
    }
    if (collect.isNotEmpty()) yield(collect)
}