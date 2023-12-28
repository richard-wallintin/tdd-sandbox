package y2023.day21

import util.CardinalDirection
import util.Point
import java.util.*

data class Garden(
    val size: Point,
    val start: Point,
    val rocks: Set<Point>
) {
    data class Step(val distance: Int, val point: Point)

    fun reach(steps: Int): Int {
        val q: Queue<Step> = LinkedList()
        q.offer(Step(0, start))

        val distances = mutableMapOf(start to 0)

        while (q.isNotEmpty()) {
            val (d, p) = q.remove()
            if(d > steps) break

            CardinalDirection.entries.map(p::go).filter { it in size }.filter { it !in rocks }
                .filter { !distances.containsKey(it) }
                .forEach {
                    distances[it] = d +1
                    q.offer(Step(d + 1, it))
                }
        }

        val inverseDistances = distances.entries.groupingBy { it.value }.eachCount()

        return IntProgression.fromClosedRange(steps.mod(2), steps, 2).sumOf {
            inverseDistances[it] ?: 0
        }
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
                size = size,
                start = start ?: throw IllegalArgumentException("no 'S' in $text"),
                rocks = rocks.toSet()
            )
        }
    }

}
