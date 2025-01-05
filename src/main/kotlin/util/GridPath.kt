package util

import java.util.*

data class GridPath(val points: List<Point>) {
    val steps get() = points.zipWithNext()

    private val location = points.last()
    val length = points.size - 1

    fun go(next: Point) = copy(points = points + next)

    fun shortestPaths(
        to: Point,
        validMove: (Point) -> Boolean = { true },
        steering: Comparator<GridPath> = Comparator.comparing { it.length + it.location.distance(to) },
    ) = sequence {
        val queue: Queue<GridPath> = PriorityQueue(steering)
        queue.offer(this@GridPath)

        val seen = mutableSetOf(location)

        while (!queue.isEmpty()) {
            val p = queue.poll()

            if (p.location == to) yield(p)

            p.location.cardinalEnv()
                .filter { it !in p.points }
                .filter(validMove)
                .filter(seen::add)
                .map(p::go)
                .forEach(queue::offer)
        }
    }

    companion object {
        fun start(from: Point) = GridPath(listOf(from))
    }
}