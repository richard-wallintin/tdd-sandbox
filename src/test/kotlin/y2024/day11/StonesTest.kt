package y2024.day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.longs
import util.repeat
import y2024.day11.Stone.Companion.blink
import y2024.day11.Stone.Companion.countStones
import y2024.day11.Stone.Companion.parseArrangement
import y2024.day11.Stone.Companion.predictStones
import y2024.day11.Stone.Companion.render

class StonesTest {

    @Test
    fun `stone 0 becomes 1`() {
        Stone(0).blink() shouldBe listOf(Stone(1))
    }

    @Test
    fun `digit split`() {
        Stone(1000).blink() shouldBe listOf(
            Stone(10), Stone(0)
        )

        Stone(10).blink() shouldBe listOf(
            Stone(1), Stone(0)
        )

        Stone(99).blink() shouldBe listOf(
            Stone(9), Stone(9)
        )
    }

    @Test
    fun `multiply by 2024`() {
        Stone(1).blink() shouldBe listOf(Stone(2024))
        Stone(999).blink() shouldBe listOf(Stone(2021976))
    }

    private val sampleArrangement = parseArrangement("0 1 10 99 999")

    @Test
    fun `one blink sample`() {
        sampleArrangement.blink().render() shouldBe "1 2024 1 0 9 9 2021976"
    }

    private val sampleArrangement2 = parseArrangement("125 17")

    @Test
    fun `6 and 25 blinks`() {
        sampleArrangement2.repeat(6) { blink() }
            .render() shouldBe "2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2"

        sampleArrangement2.countStones(25) shouldBe 55312
    }

    private val inputArrangement = parseArrangement("70949 6183 4 3825336 613971 0 15 182")

    @Test
    fun part1() {
        inputArrangement.countStones(25) shouldBe 185205
    }

    @Test
    fun `predict stone count`() {
        sampleArrangement.predictStones(1) shouldBe 7
        sampleArrangement2.predictStones(25) shouldBe 55312
    }

    @Test
    fun part2() {
        inputArrangement.predictStones(75) shouldBe 221280540398419L
    }
}

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
