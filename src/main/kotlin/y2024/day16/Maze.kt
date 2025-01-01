package y2024.day16

import util.CardinalDirection
import util.Grid
import util.Point
import util.RelativeDirection
import y2024.day16.Placement.Companion.facing
import java.util.*

data class Maze(
    val grid: Grid<Char>,
    val score: Int = 0,
    val reindeer: Placement = grid.findAll('S')
        .first().first facing CardinalDirection.E,
    private val finish: Point = grid.findAll('E').first().first
) {
    val bestPathScore: Int by lazy {
        val q = PriorityQueue(compareBy<Maze> { it.score })
        q.add(this)
        val seen = mutableMapOf(reindeer to score)

        while (q.isNotEmpty()) {
            val m = q.remove()

            if (m.reindeer.location == finish) return@lazy m.score

            q.addAll(m.next().filter {
                val best = seen.getOrDefault(it.reindeer, Int.MAX_VALUE)
                if (it.score < best) {
                    seen[it.reindeer] = it.score
                    true
                } else {
                    false
                }
            })
        }

        throw IllegalStateException("did not find a path through the maze")
    }

    private fun next() = listOfNotNull(
        move(),
        rotate(RelativeDirection.RIGHT),
        rotate(RelativeDirection.LEFT)
    )

    fun move(): Maze? {
        val newPlacement = reindeer.move()
        return if (grid[newPlacement.location] != '#')
            copy(score = score + 1, reindeer = newPlacement)
        else
            null
    }

    fun rotate(rel: RelativeDirection) = copy(score = score + 1000, reindeer = reindeer.rotate(rel))

    companion object {
        fun parse(text: String): Maze {
            val grid = Grid.charGridOf(text)
            return Maze(grid = grid)
        }
    }

}