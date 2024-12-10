package y2024.day10

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Point

class HikingTest {

    private val smallSampleMap = IslandMap.parse(
        """
                0123
                1234
                8765
                9876
            """.trimIndent()
    )

    @Test
    fun `parse map`() {
        smallSampleMap.starts shouldBe setOf(
            Point(0, 0)
        )
    }

    @Test
    fun `find trails`() {
        smallSampleMap.trailsToTop(from = Point(0, 0)).toList().first() shouldBe
                Trail(
                    listOf(
                        Point(0, 0),
                        Point(1, 0),
                        Point(2, 0),
                        Point(3, 0),
                        Point(3, 1),
                        Point(3, 2),
                        Point(3, 3),
                        Point(2, 3),
                        Point(1, 3),
                        Point(0, 3)
                    )
                )
    }

    @Test
    fun `find trailhead score`() {
        smallSampleMap.score(trailhead = Point(0, 0)) shouldBe 1
    }

    private val mediumSampleMap = IslandMap.parse(
        """
                89010123
                78121874
                87430965
                96549874
                45678903
                32019012
                01329801
                10456732
            """.trimIndent()
    )

    @Test
    fun `all trailhead scores`() {
        mediumSampleMap.totalScore() shouldBe 36
    }

    private val inputMap = IslandMap.parse(AOC.getInput("/2024/day10.txt"))

    @Test
    fun part1() {
        inputMap.totalScore() shouldBe 798
    }

    @Test
    fun rating() {
        mediumSampleMap.totalRating() shouldBe 81
    }

    @Test
    fun part2() {
        inputMap.totalRating() shouldBe 1816
    }
}
