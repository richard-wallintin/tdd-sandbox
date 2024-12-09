package util

import java.math.BigInteger

fun Int.power(exp: Int): Int = if (exp == 0) 1 else (this * this.power(exp - 1))

val Iterable<Int>.big get() = map { BigInteger.valueOf(it.toLong()) }

val Int.big: BigInteger get() = BigInteger.valueOf(toLong())

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

fun <T> List<T>.pick(n: Int): Sequence<List<T>> =
    if (n >= size) sequenceOf(this)
    else if (n == 0) sequenceOf(emptyList())
    else sequence {
        (0..size - n).forEach { i ->
            val p = get(i)
            val rest = drop(i + 1)
            rest.pick(n - 1).forEach {
                yield(listOf(p) + it)
            }
        }
    }

fun <K, V : Any> Sequence<Map<K, V>>.mergeMaps(remappingFunction: (V, V) -> V) =
    fold(mutableMapOf<K, V>()) { acc, map ->
        map.forEach { (k, v) -> acc.merge(k, v, remappingFunction) }
        acc
    }.toMap()

fun <E> List<E>.remove(idx: Int) = slice(0..<idx) + slice(idx + 1..lastIndex)

fun <T> Int.times(o: T) = (1..this).map { o }

fun <T> T.repeat(times: Int, op: T.() -> T) = (1..times).fold(this) { x, _ -> x.op() }
fun <T> T.forever(op: T.() -> T) = sequence {
    var x = this@forever
    while (true) {
        yield(x)
        x = x.op()
    }
}

fun <E> MutableList<E>.swap(a: Int, b: Int) {
    require(a < b)
    val valueA = removeAt(a)
    val valueB = removeAt(b - 1)
    add(a, valueB)
    add(b, valueA)
}

fun <E> MutableList<E>.replace(idx: Int, v: E): E {
    val oldValue = removeAt(idx)
    add(idx, v)
    return oldValue
}