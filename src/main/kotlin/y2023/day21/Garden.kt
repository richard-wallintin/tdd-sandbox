package y2023.day21

import util.CardinalDirection
import util.Point
import java.util.*

data class Garden(
    val baseSize: Point,
    val start: Point,
    val rocks: Set<Point>
) {
    data class Step(val distance: Int, val point: Point)

    fun reach(steps: Int): Int {
        val mod2 = steps.mod(2)
        return shortestPath(start).take(steps + 1).withIndex().filter {
            it.index.mod(2) == mod2
        }.sumOf { it.value }.toInt()
    }

    private fun computeDistances(from: Point, maxSteps: Int): Map<Point, Int> {
        val distances = mutableMapOf(from to 0)

        val q: Queue<Step> = LinkedList()
        q.offer(Step(0, from))
        while (q.isNotEmpty()) {
            val (d, p) = q.remove()
            if (d > maxSteps) break

            CardinalDirection.entries.map(p::go)
                .filter(::walkable)
                .filter { !distances.containsKey(it) }
                .forEach {
                    distances[it] = d + 1
                    q.offer(Step(d + 1, it))
                }
        }
        return distances
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

    fun totalReachable() = shortestPath()
        .chunked(2)
        .runningReduce { (a0, a1), (b0, b1) -> listOf(a0 + b0, a1 + b1) }
        .flatten()

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
