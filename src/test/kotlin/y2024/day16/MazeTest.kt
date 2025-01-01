package y2024.day16

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.Point.Companion.by
import util.RelativeDirection
import y2024.day16.Placement.Companion.facing

class MazeTest {

    private val sample1 = Maze.parse(
        """
                ###############
                #.......#....E#
                #.#.###.#.###.#
                #.....#.#...#.#
                #.###.#####.#.#
                #.#.#.......#.#
                #.#.#####.###.#
                #...........#.#
                ###.#.#####.#.#
                #...#.....#.#.#
                #.#.#.###.#.#.#
                #.....#...#.#.#
                #.###.#.#.#.#.#
                #S..#.....#...#
                ###############
            """.trimIndent()
    )

    @Test
    fun `parse maze`() {
        sample1.reindeer shouldBe (1 by 13).facing(CardinalDirection.E)
    }

    @Test
    fun `different movements`() {
        val start = (1 by 13).facing(CardinalDirection.E)
        start.move() shouldBe (2 by 13).facing(CardinalDirection.E)
        start.rotate(RelativeDirection.RIGHT) shouldBe (1 by 13).facing(CardinalDirection.S)
    }

    @Test
    fun `movement scoring`() {
        sample1.score shouldBe 0

        val moved = sample1.move()!!
        moved.score shouldBe 1
        moved.reindeer shouldBe sample1.reindeer.move()

        val rotated = sample1.rotate(RelativeDirection.RIGHT)
        rotated.score shouldBe 1000
        rotated.reindeer shouldBe sample1.reindeer.rotate(RelativeDirection.RIGHT)

        rotated.move() shouldBe null
    }

    private val sample2 = Maze.parse(
        """
                #################
                #...#...#...#..E#
                #.#.#.#.#.#.#.#.#
                #.#.#.#...#...#.#
                #.#.#.#.###.#.#.#
                #...#.#.#.....#.#
                #.#.#.#.#.#####.#
                #.#...#.#.#.....#
                #.#.#####.#.###.#
                #.#.#.......#...#
                #.#.###.#####.###
                #.#.#...#.....#.#
                #.#.#.#####.###.#
                #.#.#.........#.#
                #.#.#.#########.#
                #S#.............#
                #################
            """.trimIndent()
    )

    @Test
    fun search() {
        sample1.bestPathScore shouldBe 7036
        sample2.bestPathScore shouldBe 11048
    }


    private val inputMaze = Maze.parse(AOC.getInput("/2024/day16.txt"))
    @Test
    fun part1() {
        inputMaze.bestPathScore shouldBe 109496
    }

    @Test
    fun `determine all best path tiles`() {
        sample1.bestPathTiles shouldBe 45
        sample2.bestPathTiles shouldBe 64
    }

    @Test
    fun part2() {
        inputMaze.bestPathTiles shouldBe 551
    }
}
