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
        sampleTrack.cheatPaths.sorted().count() shouldBe 44
    }

    private val racetrack = Racetrack.parse(AOC.getInput("/2024/day20.txt"))

    @Test
    fun part1() {
        racetrack.cheatPaths.count { it >= 100 } shouldBe 1507
    }

    @Test
    fun `advanced cheat paths`() {
        sampleTrack.advancedCheats(50).count { it == 76 } shouldBe 3
        sampleTrack.advancedCheats(50).count { it == 74 } shouldBe 4
    }

    @Test
    fun part2() {
        racetrack.advancedCheats(100).count { it >= 100 } shouldBe 1037936
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

    fun advancedCheats(targetOptimization: Int = 50) = sequence {
        defaultPath.points.forEachIndexed { timeToP, p ->
            // we care about any chance to save at least 50 picos
            defaultPath.points.withIndex()
                .drop(timeToP + targetOptimization).reversed()
                .forEach { (defaultTimeToGoal, goal) ->

                    val shortcutDuration = p.distance(goal).toInt()
                    if (shortcutDuration <= 20) {
                        val shortcutTimeToGoal =
                            timeToP + shortcutDuration

                        if (defaultTimeToGoal > shortcutTimeToGoal) {
                            yield(defaultTimeToGoal - shortcutTimeToGoal)
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
