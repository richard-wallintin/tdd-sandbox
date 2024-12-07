package y2024.day7

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2024.day7.Equation.Companion.totalCalibrationResult

class CalibrationTest {

    private val sampleEquation = Equation.parse("190: 10 19")

    @Test
    fun `parse equation`() {
        sampleEquation.result shouldBe 190
        sampleEquation.operands shouldBe listOf(10, 19)
    }

    @Test
    fun `solve simple equation`() {
        sampleEquation.solutions() shouldBe 1
    }

    @Test
    fun `solve triple equation`() {
        Equation.parse("292: 11 6 16 20").solutions() shouldBe 1
    }

    @Test
    fun `multiple solutions`() {
        Equation.parse("3267: 81 40 27").solutions() shouldBe 2
    }

    @Test
    fun `no solutions`() {
        Equation.parse("83: 17 5").solutions() shouldBe 0
    }

    @Test
    fun `total calibration result`() {
        Equation.parseMany(
            """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent()
        ).totalCalibrationResult() shouldBe 3749
    }

    @Test
    fun part1() {
        Equation.parseMany(AOC.getInput("/2024/day7.txt"))
            .totalCalibrationResult() shouldBe 932137732557L
    }
}

