package day9

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.integers

class OasisTest {

    private val firstExample = listOf(0, 3, 6, 9, 12, 15)

    @Test
    fun `sequence normalization`() {
        firstExample.diff() shouldBe listOf(3, 3, 3, 3, 3)
        listOf(3, 3, 3, 3, 3).diff() shouldBe listOf(0, 0, 0, 0)
    }

    @Test
    fun extrapolation() {
        firstExample.next() shouldBe 18
        "1 3 6 10 15 21".integers().next() shouldBe 28
        "10 13 16 21 30 45".integers().next() shouldBe 68
    }

    @Test
    fun `extrapolation to the past`() {
        firstExample.prev() shouldBe -3
        "1 3 6 10 15 21".integers().prev() shouldBe 0
        "10 13 16 21 30 45".integers().prev() shouldBe 5
    }

    private val aocInput = AOC.getInput("/day9.txt")

    @Test
    fun `part 1`() {
        aocInput.lineSequence().map {
            it.integers().next()
        }.sum() shouldBe 1_980_437_560
    }

    @Test
    fun `part 2`() {
        aocInput.lineSequence().map { it.integers().prev() }.sum() shouldBe 977
    }
}

private fun List<Int>.prev(): Int {
    return if (allZeroes()) 0 else first() - diff().prev()
}

private fun List<Int>.next(): Int {
    return if (allZeroes()) 0 else last() + diff().next()
}


private fun List<Int>.allZeroes() = all { it == 0 }

private fun List<Int>.diff(): List<Int> {
    return this.zipWithNext { a, b -> b - a }
}
