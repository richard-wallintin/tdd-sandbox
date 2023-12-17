package day13

import AOC
import day13.Pattern.Companion.diff
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MirrorsTest {

    private val referenceInput = """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.

        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """.trimIndent()


    private val referencePatterns = Pattern.ofMany(referenceInput).toList()
    private val patternA = referencePatterns.first()
    private val patternB = referencePatterns[1]

    private val inputPatterns = Pattern.ofMany(
        AOC.getInput("/2023/day13.txt")
    )

    @Test
    fun `pattern recognition`() {
        referencePatterns.count() shouldBe 2
        patternA.row(0) shouldBe "#.##..##."
        patternA.col(0) shouldBe "#.##..#"
    }

    @Test
    fun `identify mirror column and row`() {
        patternA.mirrorColumn() shouldBe 5
        patternB.mirrorRow() shouldBe 4

        referencePatterns.sumOf { it.summary() } shouldBe 405
    }

    @Test
    fun `part 1`() {
        inputPatterns.sumOf { it.summary() } shouldBe 32035
    }

    @Test
    fun `single delta`() {
        "#.##..##.".diff("..##..##.") shouldBe 1
    }

    @Test
    fun `find mirror column with diff goal`() {
        patternA.mirrorRow(diff = 1) shouldBe 3
        patternA.mirrorColumn(diff = 1) shouldBe null

        patternB.mirrorRow(diff = 1) shouldBe 1
        patternB.mirrorColumn(diff = 1) shouldBe null

        referencePatterns.sumOf { it.summary(1) } shouldBe 400
    }

    @Test
    fun `part 2`() {
        inputPatterns.sumOf { it.summary(1) } shouldBe 24847
    }
}

