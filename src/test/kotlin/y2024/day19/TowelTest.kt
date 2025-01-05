package y2024.day19

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TowelTest {

    private val sampleTowels = listOf(
        StripePattern("r"),
        StripePattern("wr"),
        StripePattern("b"),
        StripePattern("g"),
        StripePattern("bwu"),
        StripePattern("rb"),
        StripePattern("gb"),
        StripePattern("br"),
    )

    @Test
    fun `parse patterns`() {
        StripePattern.parseMany("r, wr, b, g, bwu, rb, gb, br") shouldBe sampleTowels
    }

    private val sampleDesigns = StripePattern.parseLines(
        """
            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """.trimIndent().lineSequence()
    )

    @Test
    fun `find possible combinations`() {
        sampleDesigns.count { it.canBeArranged(sampleTowels) } shouldBe 6
    }

    private val input = AOC.getInput("/2024/day19.txt").lineSequence()
    private val inputTowels = StripePattern.parseMany(input.first())

    private val inputDesigns = input.drop(2).map { StripePattern(it) }

    @Test
    fun `find solutions quicker`() {
        inputDesigns.first().canBeArranged(inputTowels) shouldBe false
    }

    @Test
    fun part1() {
        val cache = mutableMapOf<StripePattern, Long>()
        inputDesigns.count { it.canBeArranged(inputTowels, cache) } shouldBe 317
    }

    @Test
    fun `count arrangements`() {
        sampleDesigns.sumOf { it.countArrangements(sampleTowels) } shouldBe 16
        inputDesigns.first().countArrangements(inputTowels) shouldBe 0
    }

    @Test
    fun part2() {
        val cache = mutableMapOf<StripePattern, Long>()
        inputDesigns.sumOf {
            it.recursiveCountArrangements(
                inputTowels,
                cache
            )
        } shouldBe 883443544805484L
    }
}
