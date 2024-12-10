package y2024.day10

import util.CardinalDirection
import util.Grid
import util.Point

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
            return IslandMap(Grid.charGridOf(text).map { "$it".toInt() })
        }
    }
}