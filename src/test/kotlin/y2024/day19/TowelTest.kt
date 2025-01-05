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

@JvmInline
value class StripePattern(private val colors: String) {
    private val length get() = colors.length

    fun canBeArranged(
        patterns: List<StripePattern>,
        cache: MutableMap<StripePattern, Long> = mutableMapOf(),
    ): Boolean {
        return recursiveCountArrangements(patterns, cache) > 0
    }

    fun countArrangements(patterns: List<StripePattern>): Long {
        return recursiveCountArrangements(patterns)
    }

    fun recursiveCountArrangements(
        patterns: List<StripePattern>,
        cache: MutableMap<StripePattern, Long> = mutableMapOf(),
    ): Long {
        return if (length == 0) 1L
        else cache[this] ?: run {
            patterns.filter {
                isPrefix(it)
            }.sumOf {
                removePrefix(it).recursiveCountArrangements(patterns, cache)
            }
        }.also { cache[this] = it }
    }

    private fun removePrefix(p: StripePattern) = StripePattern(colors.removePrefix(p.colors))
    private fun isPrefix(prefix: StripePattern): Boolean = colors.startsWith(prefix.colors)

    override fun toString() = colors

    companion object {
        fun parseMany(line: String): List<StripePattern> {
            return line.split(Regex(", ")).map { StripePattern(it) }
        }

        fun parseLines(lines: Sequence<String>) = lines.map { StripePattern(it) }
    }
}
