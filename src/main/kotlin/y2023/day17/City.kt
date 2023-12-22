package y2023.day17

import util.CardinalDirection
import util.Point
import util.transpose
import java.util.*

class City(val matrix: List<List<Int>>) {
    val averageLoss by lazy {
        matrix.flatten().average().toInt()
    }

    fun lossAt(x: Long, y: Long): Int? {
        return matrix.getOrNull(x.toInt())?.getOrNull(y.toInt())
    }

    fun findShortestPath(
        start: Point = Point(0, 0),
        dest: Point = Point(matrix.size - 1, matrix.first().size - 1),
        policy: Policy = Policy(0, 3),
    ) = findShortestPaths(start, dest, policy).first().totalLoss

    private fun findShortestPaths(
        start: Point,
        dest: Point,
        policy: Policy,
        costEstimate: Int = 1,
        seen: MutableSet<Trajectory> = mutableSetOf()
    ) = sequence {
        val queue = PriorityQueue<Path>(compareBy { it.heuristicLoss(dest, costEstimate) })
        queue.addAll(
            listOf(CardinalDirection.E, CardinalDirection.S).map {
                Path(start, it, policy = policy)
            })

        while (queue.isNotEmpty()) {
            val p = queue.remove()
            if (p.to == dest && p.canStop) yield(p)
            else queue.addAll(
                p.next { lossAt(it.x, it.y) }.filter { seen.add(it.trajectory) }
            )
        }
    }

    companion object {
        fun of(text: String): City {
            return City(matrix = text.lines()
                .map { it.map { c -> c.toString().toInt() } }
                .transpose()
            )
        }
    }
}
