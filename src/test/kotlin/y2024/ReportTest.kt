package y2024

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ReportTest {

    private val sampleReports = listOf(
        Report(listOf(7, 6, 4, 2, 1)),
        Report(listOf(1, 2, 7, 8, 9)),
        Report(listOf(9, 7, 6, 2, 1)),
        Report(listOf(1, 3, 2, 4, 5)),
        Report(listOf(8, 6, 4, 4, 1)),
        Report(listOf(1, 3, 6, 7, 9))
    )

    @Test
    fun `can parse reports`() {
        Report.parse(
            """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent()
        ) shouldBe sampleReports
    }

    @Test
    fun safety() {
        Report(listOf(7, 6, 4, 2, 1)).safe shouldBe true
        Report(listOf(1, 2, 7, 8, 9)).safe shouldBe false
        Report(listOf(9, 7, 6, 2, 1)).safe shouldBe false
        Report(listOf(1, 3, 2, 4, 5)).safe shouldBe false
        Report(listOf(8, 6, 4, 4, 1)).safe shouldBe false
        Report(listOf(1, 3, 6, 7, 9)).safe shouldBe true
    }

    private val inputReports = Report.parse(AOC.getInput("2024/day2.txt"))

    @Test
    fun part1() {
        inputReports.count { it.safe } shouldBe 369
    }

    @Test
    fun dampenedSafety() {
        sampleReports.count { it.dampenedSafe } shouldBe 4
    }

    @Test
    fun part2() {
        inputReports.count { it.dampenedSafe } shouldBe 428
    }
}