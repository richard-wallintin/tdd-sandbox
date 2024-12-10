package y2024.day10

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.Grid
import util.Grid.Companion.charGridOf
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

data class Trail(val path: List<Point>) {
    constructor(start: Point) : this(listOf(start))

    operator fun plus(next: Point) = copy(path = path + next)

    val end = path.last()
}

data class IslandMap(private val grid: Grid<Int>) {
    fun trailsToTop(from: Point): Sequence<Trail> {
        return next(from = Trail(from)).flatMap(::trailsToTop)
    }

    private fun trailsToTop(t: Trail): Sequence<Trail> =
        if (grid[t.end] == 9) sequenceOf(t)
        else next(t).flatMap(this::trailsToTop)

    private fun next(from: Trail): Sequence<Trail> {
        val cur = from.end
        return CardinalDirection.entries.asSequence().mapNotNull { dir ->
            val next = cur.go(dir)
            val nextHeight = grid[cur]
            if (nextHeight != null && grid[next] == (nextHeight + 1)) {
                from + next
            } else null
        }
    }

    fun score(trailhead: Point) = trailsToTop(trailhead).map { it.end }.toSet().size
    fun rating(trailhead: Point) = trailsToTop(trailhead).count()

    fun totalScore() = starts.map(::score).sum()
    fun totalRating() = starts.map(::rating).sum()


    val starts: Set<Point> = grid.findAll(0).map { it.first }.toSet()

    companion object {
        fun parse(text: String): IslandMap {
            return IslandMap(charGridOf(text).map { "$it".toInt() })
        }
    }
}
