package day11

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class UniverseTest {

    private val sampleUnexpanded = Universe.parse(
        """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()
    )

    private val sampleExpanded = sampleUnexpanded.expand()

    @Test
    fun `parse image`() {
        sampleUnexpanded.rows shouldBe 10
        sampleUnexpanded.columns shouldBe 10
        sampleUnexpanded.galaxies shouldBe 9

        sampleUnexpanded.locationOfGalaxy(1) shouldBe Coord(row = 0, col = 3)
    }

    @Test
    fun `empty rows and columns`() {
        sampleUnexpanded.emptyRows shouldBe 2
        sampleUnexpanded.emptyColumns shouldBe 3
    }

    @Test
    fun expand() {
        val expanded = sampleExpanded
        expanded.rows shouldBe 12
        expanded.columns shouldBe 13
        expanded.galaxies shouldBe 9
        expanded.locationOfGalaxy(1) shouldBe Coord(row = 0, col = 4)
        expanded.locationOfGalaxy(9) shouldBe Coord(row = 11, col = 5)
    }

    @Test
    fun `shortest path`() {
        sampleUnexpanded.shortestPath(1, 2) shouldBe 5
        sampleExpanded.shortestPath(5, 9) shouldBe 9
        sampleExpanded.shortestPath(1, 7) shouldBe 15
        sampleExpanded.shortestPath(3, 6) shouldBe 17
        sampleExpanded.shortestPath(8, 9) shouldBe 5
    }

    @Test
    fun `sum of shortest paths`() {
        sampleExpanded.totalShortestPaths() shouldBe 374
    }

    @Test
    fun `part 1`() {
        Universe.parse(AOC.getInput("/day11.txt")).expand().totalShortestPaths() shouldBe 10_154_062
    }
}