package y2023.day23

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.Edge
import util.Point
import util.WeightedGraph

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
    @Disabled("works but is to slow")
    fun `part 2`() {
        islandMap.longestHikeLengthIgnoringSlopes shouldBe 6350
    }

    @Test
    fun `convert map to weighted graph`() {
        IslandMap.of(
            """
            #.###
            #..##
            ##..#
            ###.#
            ###.#
        """.trimIndent()
        ).toGraph(true) shouldBe WeightedGraph(
            setOf(
                Edge(Point(1, 0), Point(3, 4), 6)
            )
        )
    }

    @Test
    fun `convert to complex graph`() {
        val graph = IslandMap.of(
            """
            #.###
            .....
            .###.
            .....
            ##.##
        """.trimIndent()
        ).toGraph(true)

        graph shouldBe WeightedGraph(
            setOf(
                Edge(Point(1, 0), Point(1, 1), 1),
                Edge(Point(1, 1), Point(2, 3), 5),
                Edge(Point(1, 1), Point(2, 3), 7),
                Edge(Point(2, 3), Point(2, 4), 1),
            )
        )

        graph.shortestPath(Point(1, 0), Point(2, 4), Point::distance) shouldBe 7
    }

    @Test
    fun `find longest path`() {
        WeightedGraph(
            setOf(
                Edge(Point(1, 0), Point(1, 1), 1),
                Edge(Point(1, 1), Point(2, 3), 5),
                Edge(Point(1, 1), Point(2, 3), 7),
                Edge(Point(2, 3), Point(2, 4), 1),
            )
        ).longestPath(Point(1, 0), Point(2, 4)) shouldBe 9
    }
}