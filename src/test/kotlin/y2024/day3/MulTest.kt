package y2024.day3

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
        ) shouldBe listOf(Multiply(2, 4), Multiply(5, 5), Multiply(11, 8), Multiply(8, 5))

    }

    @Test
    fun `compute sum of multiples`() {
        computeSumOfMultiples(extractPairs(sampleMemory)) shouldBe 161
    }

    private fun computeSumOfMultiples(pairs: List<Instruction>) =
        pairs.filterIsInstance<Multiply>().sumOf { it.compute() }

    private fun extractPairs(memory: String) =
        extractInstructions(memory).filterIsInstance<Multiply>()

    @Test
    fun part1() {
        extractPairs(AOC.getInput("2024/day3.txt")).let(::computeSumOfMultiples) shouldBe 182619815
    }

    @Test
    fun `extract instructions`() {
        extractInstructions("mul(1,1)//do()//don't()") shouldBe listOf(
            Multiply(1, 1), Enable, Disable
        )
    }

    private fun extractInstructions(memory: String): List<Instruction> {
        return Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)|(do)\\(\\)|(don't)\\(\\)").findAll(
            memory
        ).map {
            if (it.groupValues[1].isNotBlank())
                Multiply(it.groupValues[1].toInt(), it.groupValues[2].toInt())
            else if (it.groupValues[3].isNotBlank())
                Enable
            else
                Disable

        }
            .toList()
    }

    private val secondSampleMemory =
        "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    @Test
    fun `compute sum of enabled multiples`() {
        computeAdvancedSumOfMultiples(extractInstructions(secondSampleMemory)) shouldBe 48
    }

    private fun computeAdvancedSumOfMultiples(instructions: List<Instruction>): Int {
        var enabled = true
        var sum = 0
        instructions.forEach { instruction ->
            when (instruction) {
                is Enable -> enabled = true
                is Disable -> enabled = false
                is Multiply -> if (enabled) sum += instruction.compute()
            }
        }
        return sum
    }

    @Test
    fun part2() {
        extractInstructions(AOC.getInput("2024/day3.txt")).let(::computeAdvancedSumOfMultiples) shouldBe 80747545
    }
}

sealed interface Instruction
data class Multiply(val a: Int, val b: Int) : Instruction {
    fun compute() = a * b
}

data object Enable : Instruction
data object Disable : Instruction
