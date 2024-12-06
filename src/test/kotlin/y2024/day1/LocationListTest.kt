package y2024.day1

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LocationListTest {
    private val referenceList = LocationList(
        listOf(
            Pair(3, 4),
            Pair(4, 3),
            Pair(2, 5),
            Pair(1, 3),
            Pair(3, 9),
            Pair(3, 3)
        )
    )

    @Test
    fun `two lists`() {
        LocationList.parse(
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent()
        ) shouldBe referenceList
    }

    @Test
    fun totalDistance() {
        LocationList(
            listOf(
                Pair(3, 4),
                Pair(4, 3),
                Pair(2, 5),
                Pair(1, 3),
                Pair(3, 9),
                Pair(3, 3)
            )
        ).sort().totalDistance() shouldBe 11
    }

    private val inputList = LocationList.parse(AOC.getInput("/2024/day1.txt"))

    @Test
    fun part1() {
        inputList.sort().totalDistance() shouldBe 3508942
    }

    @Test
    fun `similarity scoring`() {
        referenceList.similarityScore() shouldBe 31
    }

    @Test
    fun part2() {
        inputList.similarityScore() shouldBe 26593248
    }
}