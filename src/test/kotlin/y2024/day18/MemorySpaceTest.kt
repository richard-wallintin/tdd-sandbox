package y2024.day18

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Point
import util.Point.Companion.by

class MemorySpaceTest {

    private val sampleBytes = MemorySpace.readPoints(
        """
                5,4
                4,2
                4,5
                3,0
                2,1
                6,3
                2,4
                1,5
                0,6
                3,3
                2,6
                5,1
                1,2
                5,5
                2,5
                6,5
                1,4
                0,4
                6,4
                1,1
                6,1
                1,0
                0,5
                1,6
                2,0
            """.trimIndent()
    )

    private val sample12 = MemorySpace(7 by 7).dropAll(sampleBytes.take(12))

    @Test
    fun `drop bytes into memory`() {
        sample12.isSafe(0 by 0) shouldBe true
        sample12.isSafe(0 by 6) shouldBe false
        sample12.isSafe(0 by 7) shouldBe false // out of space
    }

    @Test
    fun `find shortest path`() {
        sample12.shortestPath() shouldBe 22
    }


    private val inputBytes = MemorySpace.readPoints(AOC.getInput("/2024/day18.txt"))
    private val largeSpace = MemorySpace(71 by 71)

    @Test
    fun part1() {
        largeSpace.dropAll(inputBytes.take(1024)).shortestPath() shouldBe 292
    }

    @Test
    fun part2() {
        val start = largeSpace.dropAll(inputBytes.take(1024))
        val blocked = inputBytes.drop(1024).runningFold(start, MemorySpace::drop).dropWhile {
            it.shortestPath() != null
        }.first()

        blocked.lastDrop shouldBe Point(58, 44)
    }
}
