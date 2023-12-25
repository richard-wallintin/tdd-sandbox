package util

import java.math.BigInteger

fun Int.power(exp: Int): Int = if (exp == 0) 1 else (this * this.power(exp - 1))

fun String.split() = split(Regex("\\s+")).filter(String::isNotBlank)

fun String.integers() = split().integers()
fun Iterable<String>.integers() = filter { it.isNotBlank() }.map { it.toInt() }

fun String.longs() = split().longs()
fun Iterable<String>.longs() = filter { it.isNotBlank() }.map { it.toLong() }

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

fun <T> List<List<T>>.transpose(): List<List<T>> {
    return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
}

fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    val gcd = a.gcd(b)
    val absProduct = a.multiply(b).abs()
    return absProduct.divide(gcd)
}