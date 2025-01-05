package y2024.day20

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
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
