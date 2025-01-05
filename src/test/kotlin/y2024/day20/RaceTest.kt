package y2024.day20

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Grid
import util.GridPath
import util.Point

class RaceTest {

    private val sampleTrack = Racetrack.parse(
        """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent()
    )

    @Test
    fun `parse map`() {
        sampleTrack.start shouldBe Point(1, 3)
        sampleTrack.end shouldBe Point(5, 7)
    }

    @Test
    fun `find shortest path without cheating`() {
        sampleTrack.defaultTime shouldBe 84
    }

    @Test
    fun `find cheat paths`() {
        sampleTrack.cheatPaths.sorted().onEach { println("$it") }.count() shouldBe 44
    }

    @Test
    fun part1() {
        val racetrack = Racetrack.parse(AOC.getInput("/2024/day20.txt"))

        racetrack.cheatPaths.count { it >= 100 } shouldBe 1507
    }
}

data class Racetrack(
    val start: Point,
    val end: Point,
    val map: Grid<Boolean>,
) {
    private fun isTrack(it: Point) = map[it] ?: false
    private fun isWall(it: Point) = !isTrack(it)

    private val defaultPath: GridPath by lazy {
        GridPath.start(start).shortestPaths(end, validMove = ::isTrack).first()
    }

    val defaultTime: Int get() = defaultPath.length

    val cheatPaths = sequence {
        defaultPath.steps.forEachIndexed { i, (a, b) ->
            val normalDirection = a.direction(b)
            val timeToB = i + 1
            listOf(
                normalDirection.left,
                normalDirection.right,
                normalDirection.inverse
            ).forEach { cheatDirection ->
                val w = a.go(cheatDirection)
                val x = w.go(cheatDirection)

                if (isWall(w) && isTrack(x)) {
                    val shortcutTimeToX = timeToB + 2
                    val normalTimeToX = defaultPath.points.indexOf(x) + 1
                    if (normalTimeToX >= shortcutTimeToX) {
                        yield(normalTimeToX - shortcutTimeToX)
                    }
                }
            }
        }
    }

    companion object {
        fun parse(text: String): Racetrack {
            val grid = Grid.charGridOf(text)

            return Racetrack(
                grid.findAll('S').first().first,
                grid.findAll('E').first().first,
                grid.map {
                    when (it) {
                        'S', 'E', '.' -> true
                        else -> false
                    }
                }
            )
        }
    }

}
