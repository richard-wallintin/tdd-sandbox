package y2024

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MulTest {

    private val sampleMemory =
        "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

    @Test
    fun `extract number pairs from memory`() {
        extractPairs(
            sampleMemory
        ) shouldBe listOf(Pair(2, 4), Pair(5, 5), Pair(11, 8), Pair(8, 5))

    }

    @Test
    fun `compute sum of multiples`() {
        computeSumOfMultiples(extractPairs(sampleMemory)) shouldBe 161
    }

    private fun computeSumOfMultiples(pairs: List<Pair<Int, Int>>) =
        pairs.sumOf { it.first * it.second }

    private fun extractPairs(memory: String) = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)").findAll(
        memory
    ).map { Pair(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
        .toList()

    @Test
    fun part1() {
        extractPairs(AOC.getInput("2024/day3.txt")).let(::computeSumOfMultiples) shouldBe 182619815
    }
}