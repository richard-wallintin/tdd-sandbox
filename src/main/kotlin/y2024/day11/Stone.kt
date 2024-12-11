package y2024.day11

import util.longs
import util.repeat

typealias Arrangement = List<Stone>

data class Stone(val engraved: Long) {
    fun blink(): List<Stone> {
        val digits = engraved.toString(10)
        return if (digits.length % 2 == 0) {
            val middle = digits.length / 2
            listOf(
                Stone(digits.substring(0..<middle).toLong()),
                Stone(digits.substring(middle).toLong())
            )
        } else if (engraved == 0L) {
            listOf(Stone(1))
        } else {
            listOf(Stone(engraved * 2024))
        }
    }

    fun predictStones(blinks: Int): Long {
        return if (blinks == 0) 1 else {
            val key = engraved to blinks
            cache[key] ?: blink().sumOf {
                it.predictStones(blinks - 1)
            }.also { cache[key] = it }
        }
    }

    companion object {
        fun parseArrangement(line: String) = line.longs().map { Stone(it) }
        fun Arrangement.blink() = flatMap { it.blink() }
        fun Arrangement.render() = joinToString(" ") {
            it.engraved.toString()
        }

        fun Arrangement.countStones(blinks: Int) = repeat(blinks) { blink() }.size

        private val cache = mutableMapOf<Pair<Long, Int>, Long>()

        fun Arrangement.predictStones(blinks: Int) = this.sumOf { it.predictStones(blinks) }
    }

}