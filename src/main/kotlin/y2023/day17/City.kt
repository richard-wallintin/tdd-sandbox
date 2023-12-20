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
        val cache = mutableMapOf<Trajectory, Path>()

        val init = Trajectory(from = start, CardinalDirection.E, 0)

        val guide =
            findShortestPath(init, dest, cache, 9).take(100).minBy { it.totalLoss }

        println("${guide.summary} - ${guide.totalLoss}")

        guide.traverseBackwards().forEach {
            val strictylBest = findShortestPath(it.trajectory, dest, cache, 1).first()
            println(strictylBest)
            cache[it.trajectory] = strictylBest
        }

        return cache[init]?.totalLoss ?: Int.MAX_VALUE
    }

    private fun findShortestPath(
        start: Trajectory,
        dest: Point,
        cache: MutableMap<Trajectory, Path>,
        costEstimate: Int
    ) = sequence {

        val queue = PriorityQueue<Path>(compareBy { it.heuristicLoss(dest, costEstimate) })
        queue.add(Path(start))

        while (queue.isNotEmpty()) {
            val p = queue.remove()
            // println(p.summary)
            if (p.to == dest) yield(p)

            cache[p.trajectory]?.let {
                yield(p + it)
            }

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
