package y2023.day11

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

    private val inputUniverse = Universe.parse(AOC.getInput("/2023/day11.txt"))

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
        sampleUnexpanded.expand(factor = 10).totalShortestPaths() shouldBe 1030
        sampleUnexpanded.expand(factor = 100).totalShortestPaths() shouldBe 8410
    }

    @Test
    fun `factored expansion`() {
        val exp = sampleUnexpanded.expand(factor = 10)
        exp.locationOfGalaxy(1) shouldBe Coord(0, 12)
    }


    @Test
    fun `part 1`() {
        inputUniverse.expand().totalShortestPaths() shouldBe 10_154_062
    }

    @Test
    fun `part 2`() {
        inputUniverse.expand(factor = 1_000_000).totalShortestPaths() shouldBe 553_083_047_914L
    }
}