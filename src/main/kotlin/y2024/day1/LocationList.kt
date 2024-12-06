package y2024.day1

import util.integers
import kotlin.math.abs

data class LocationList(val list: List<Pair<Int, Int>>) {
    fun totalDistance(): Int {
        return list.sumOf { (a, b) -> abs(a - b) }
    }

    private val left = list.map { it.first }
    private val right = list.map { it.second }

    fun sort(): LocationList {
        return LocationList(left.sorted().zip(right.sorted()))
    }

    fun similarityScore(): Int {
        return left.sumOf { l -> l * right.count { it == l } }
    }

    companion object {
        fun parse(str: String): LocationList {
            return str.lineSequence().filter { it.isNotBlank() }.map {
                val (a, b) = it.integers()
                Pair(a, b)
            }.toList().let(::LocationList)
        }
    }
}