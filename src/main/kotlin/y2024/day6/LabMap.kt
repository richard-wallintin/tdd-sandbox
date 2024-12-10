package y2024.day6

import util.*

data class LabMap(
    val size: Point,
    val guard: Point,
    val guardDirection: CardinalDirection,
    val obstacles: Set<Point> = emptySet()
) {
    private val area = Rectangle(Point(0, 0), size)

    val guardIsGone = guard !in area

    fun step(): LabMap {
        val ahead = guard.go(guardDirection)
        return if (ahead in obstacles)
            copy(guardDirection = guardDirection.turn(RelativeDirection.RIGHT))
        else copy(guard = ahead)
    }

    private fun steps() = forever { step() }

    fun allGuardPositions() = guardPositions().toSet()

    private fun guardPositions() = stepsUntilGuardLeaves()
        .map { it.guard }

    private fun stepsUntilGuardLeaves() = steps()
        .takeWhile { !it.guardIsGone }

    fun obstruct(point: Point) = copy(obstacles = obstacles + point)

    val loops: Boolean by lazy {
        val visited = mutableSetOf<Pair<Point, CardinalDirection>>()
        stepsUntilGuardLeaves().forEach {
            val firstTime = visited.add(it.guard to it.guardDirection)
            if (!firstTime) return@lazy true
        }
        false
    }

    val loopingVariations: Int by lazy {
        stepsUntilGuardLeaves().map { it.guard }.toSet()
            .filter { it != guard }
            .filter { it !in obstacles }
            .count { obstruct(it).loops }
    }

    companion object {
        fun parse(text: String): LabMap {
            return of(Grid.charGridOf(text))
        }

        fun of(chars: Grid<Char>): LabMap {
            val guardPositionAndChar = chars.findAll('^', 'v', '<', '>').first()
            val guardDirection = when (val it = guardPositionAndChar.second) {
                '^' -> CardinalDirection.N
                'v' -> CardinalDirection.S
                '<' -> CardinalDirection.W
                '>' -> CardinalDirection.E
                else -> throw IllegalArgumentException("not a guard symbol: $it")
            }

            return LabMap(
                chars.size,
                guardPositionAndChar.first,
                guardDirection,
                chars.findAll('#', 'O').map { it.first }.toSet()
            )
        }
    }
}