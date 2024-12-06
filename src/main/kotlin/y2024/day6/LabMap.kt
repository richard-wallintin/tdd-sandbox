package y2024.day6

import util.*

data class LabMap(
    val size: Point,
    val guard: Point,
    val guardDirection: CardinalDirection,
    val obstacles: Set<Point> = emptySet()
) {
    val guardIsGone = guard !in Rectangle(Point(0, 0), size)

    fun step(): LabMap {
        val ahead = guard.go(guardDirection)
        return if (ahead in obstacles)
            copy(guardDirection = guardDirection.turn(RelativeDirection.RIGHT))
        else copy(guard = ahead)
    }

    fun steps() = forever { step() }

    fun allGuardPositions() = steps()
        .takeWhile { !it.guardIsGone }
        .map { it.guard }
        .toSet()

    companion object {
        fun parse(text: String): LabMap {
            return of(Grid(text.lines().map { it.toList() }))
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
                chars.findAll('#').map { it.first }.toSet()
            )
        }
    }
}