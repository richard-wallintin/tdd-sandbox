package y2023.day23

import AOC
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.Point

class HikeTest {

    private val referenceIsland = IslandMap.of(
        """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
        """.trimIndent()
    )

    @Test
    fun `parse map`() {
        val map = referenceIsland

        map.start shouldBe Point(1, 0)
        map.finish shouldBe Point(21, 22)
        map.tile(Point(1, 1)) shouldBe Tile(path = true)
        map.tile(Point(0, 0)) shouldBe Tile(path = false)
        map.tile(Point(10, 3)) shouldBe Tile(path = true, slope = CardinalDirection.E)
    }

    @Test
    fun `find longest hike`() {
        referenceIsland.longestHikeLength shouldBe 94
    }

    private val islandMap = IslandMap.of(AOC.getInput("2023/day23.txt"))

    @Test
    fun `part 1`() {
        islandMap.longestHikeLength shouldBe 2106
    }

    @Test
    fun `longest hike ignoring slopes`() {
        referenceIsland.longestHikeLengthIgnoringSlopes shouldBe 154
    }

    @Test
    fun `part 2`() {
        islandMap.longestHikeLengthIgnoringSlopes.also(::println) shouldBeGreaterThan 2106
    }
}