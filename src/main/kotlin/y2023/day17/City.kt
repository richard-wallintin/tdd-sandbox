package y2023.day17

import util.CardinalDirection
import util.Point
import util.transpose
import java.util.*

class City(val matrix: List<List<Int>>) {
    val averageLoss by lazy {
        matrix.flatten().average().toInt()
    }

    fun lossAt(x: Int, y: Int): Int? {
        return matrix.getOrNull(x)?.getOrNull(y)
    }

    fun findShortestPath(
        start: Point,
        dest: Point
    ): Int {
        val init = Trajectory(from = start, CardinalDirection.E, 0)

        return findShortestPath(init, dest, 1).first().totalLoss
    }

    private fun findShortestPath(
        start: Trajectory,
        dest: Point,
        costEstimate: Int
    ) = sequence {

        val queue = PriorityQueue<Path>(compareBy { it.heuristicLoss(dest, costEstimate) })
        queue.add(Path(start))

        while (queue.isNotEmpty()) {
            val p = queue.remove()
            // println(p.summary)
            if (p.to == dest) yield(p)

            queue.addAll(p.next { lossAt(it.x, it.y) })
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
