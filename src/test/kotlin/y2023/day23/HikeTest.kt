package y2023.day23

import AOC
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
        referenceIsland.hikeLength shouldBe 94
    }

    @Test
    fun `part 1`() {
        IslandMap.of(AOC.getInput("2023/day23.txt")).hikeLength shouldBe 2106
    }
}