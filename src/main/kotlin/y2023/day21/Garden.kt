package y2023.day21

import util.CardinalDirection
import util.Point

data class Garden(
    val baseSize: Point,
    val start: Point,
    val rocks: Set<Point>
) {
    data class Step(val distance: Int, val point: Point)

    fun reach(steps: Int): Int {
        return totalReachable(start).take(steps + 1).last().toInt()
    }

    private fun walkable(it: Point) = it.mod(baseSize) !in rocks

    fun shortestPath(from: Point = start): Sequence<Long> = sequence {
        yield(1L)

        val seen = mutableSetOf(from)
        var nextPoints = listOf(from)

        while (nextPoints.isNotEmpty()) {
            nextPoints = nextPoints.flatMap { p ->
                CardinalDirection.entries.map(p::go)
                    .filter(::walkable)
                    .filter(seen::add)
            }

            yield(nextPoints.size.toLong())
        }
    }

    fun totalReachable(from: Point = start) = shortestPath(from)
        .chunked(2)
        .runningReduce { (a0, a1), (b0, b1) -> listOf(a0 + b0, a1 + b1) }
        .flatten()

    fun findParabola(steps: Long): Parabola {
        val mod = steps.mod(baseSize.x)
        val halfSize = baseSize.x / 2
        assert(mod == halfSize)
        assert(baseSize.y / 2 == halfSize)
        assert(start.x == halfSize)

        val (p1, p2, p3) = totalReachable().withIndex()
            .take((5 * baseSize.x + mod + 1).toInt()).filter {
                it.index.toLong().mod(baseSize.x) == mod
            }.drop(2).take(3).map { Point(it.index.toLong(), it.value) }.toList()

        return Parabola.of(p1, p2, p3)
    }

    companion object {
        fun of(text: String): Garden {
            var start: Point? = null
            val rocks = mutableSetOf<Point>()
            val size = text.lineSequence().withIndex().map { (y, line) ->
                y to line.withIndex().maxOf { (x, c) ->
                    if (c == 'S') start = Point(x, y)
                    if (c == '#') rocks.add(Point(x, y))
                    x
                }
            }.map(::Point).max() + Point(1, 1)

            return Garden(
                baseSize = size,
                start = start ?: throw IllegalArgumentException("no 'S' in $text"),
                rocks = rocks.toSet()
            )
        }
    }

}
