package y2024.day16

import util.CardinalDirection
import util.Grid
import util.Grid.Companion.asText
import util.Point
import util.RelativeDirection
import y2024.day16.Placement.Companion.facing
import java.util.*

data class Maze(
    val grid: Grid<Char>,
    val score: Int = 0,
    val reindeer: Placement = grid.findAll('S')
        .first().first facing CardinalDirection.E,
    private val finish: Point = grid.findAll('E').first().first,
    val seen: Set<Point> = setOf(reindeer.location)
) {

    val bestPathTiles: Int by lazy {
        bestPaths().map { it.seen }.reduce(Set<Point>::plus).size
    }

    val bestPathScore: Int by lazy {
        bestPaths().first().score
    }

    private fun bestPaths() = sequence {
        val q = PriorityQueue(compareBy<Maze> { it.score })
        q.add(this@Maze)
        val seen = mutableMapOf(reindeer to score)
        var bestTotalScore = Int.MAX_VALUE

        while (q.isNotEmpty()) {
            val m = q.remove()

            if (m.reindeer.location == finish && m.score <= bestTotalScore) {
                bestTotalScore = m.score
                yield(m)
            }

            q.addAll(m.next().filter {
                val best = seen.getOrDefault(it.reindeer, Int.MAX_VALUE)
                if (it.score <= best) {
                    seen[it.reindeer] = it.score
                    true
                } else {
                    false
                }
            })
        }
    }

    private fun next() = listOfNotNull(
        move(),
        rotate(RelativeDirection.RIGHT),
        rotate(RelativeDirection.LEFT)
    )

    fun move(): Maze? {
        val newPlacement = reindeer.move()
        return if (grid[newPlacement.location] != '#')
            copy(score = score + 1, reindeer = newPlacement, seen = seen + newPlacement.location)
        else
            null
    }

    fun rotate(rel: RelativeDirection) = copy(score = score + 1000, reindeer = reindeer.rotate(rel))

    override fun toString(): String {
        return "SCORE: ${score}\n" + grid.plus(seen.associateWith { 'O' }).asText()
    }

    companion object {
        fun parse(text: String): Maze {
            val grid = Grid.charGridOf(text)
            return Maze(grid = grid)
        }
    }

}