package day1

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CalibrationTest {

    @Test
    fun `some examples`() {
        "1abc2".extractCalibrationValue() shouldBe 12
        "pqr3stu8vwx".extractCalibrationValue() shouldBe 38
        "a1b2c3d4e5f".extractCalibrationValue() shouldBe 15
        "treb7uchet".extractCalibrationValue() shouldBe 77
    }

    val input = AOC.getInput("/day1.txt")

    @Test
    fun `part 1`() {
        input.lineSequence().map { it.extractCalibrationValue() }.sum() shouldBe 55_090
    }

    @Test
    fun `detect named  digits`() {
        "two1nine".extractCalibrationValue2() shouldBe 29
        "eightwothree".extractCalibrationValue2() shouldBe 83
        "eighthree".extractCalibrationValue2() shouldBe 83

        """
                two1nine
                eightwothree
                abcone2threexyz
                xtwone3four
                4nineeightseven2
                zoneight234
                7pqrstsixteen
            """.trimIndent().calibrationSum2() shouldBe 281
    }

    @Test
    fun `part two`() {
        input.calibrationSum2() shouldBe 54_845
    }

    private fun String.calibrationSum2() =
        this.lineSequence().map { it.extractCalibrationValue2() }.sum()

    private fun String.extractCalibrationValue() = toList()
        .filter { it.isDigit() }
        .map { it.toString().toInt() }
        .let { it.first() * 10 + it.last() }

    val digits = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    ) + (1..9).associateBy { it.toString() }

    private fun String.extractCalibrationValue2(): Int {
        val expression = digits.keys.joinToString("|")
        val regex = Regex(expression)

        return regex.findAllWithOverlap(this).toList().let {
            it.first().value.asDigit() to it.last().value.asDigit()
        }.also { println(this to it) }
            .let {
                it.first * 10 + it.second
            }
    }

    private fun String.asDigit(): Int {
        return digits[this]!!
    }

    fun Regex.findAllWithOverlap(input: CharSequence, startIndex: Int = 0): Sequence<MatchResult> {
        if (startIndex < 0 || startIndex > input.length) {
            throw IndexOutOfBoundsException("Start index out of bounds: $startIndex, input length: ${input.length}")
        }
        return generateSequence({ find(input, startIndex) },  { find(input, it.range.first + 1) })
    }
}
