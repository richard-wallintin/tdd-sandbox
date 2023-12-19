package y2023.day16

import util.Direction

data class Tile(val type: Char) {
    fun divert(dir: Direction): List<Direction> {
        when (type) {
            '|' -> {
                if (dir == Direction.W || dir == Direction.E)
                    return listOf(Direction.N, Direction.S)
            }

            '-' -> {
                if (dir == Direction.N || dir == Direction.S)
                    return listOf(Direction.W, Direction.E)
            }

            '/' -> {
                return when (dir) {
                    Direction.W -> listOf(Direction.S)
                    Direction.E -> listOf(Direction.N)
                    Direction.N -> listOf(Direction.E)
                    Direction.S -> listOf(Direction.W)
                }
            }

            '\\' -> {
                return when (dir) {
                    Direction.W -> listOf(Direction.N)
                    Direction.E -> listOf(Direction.S)
                    Direction.N -> listOf(Direction.W)
                    Direction.S -> listOf(Direction.E)
                }
            }
        }
        return listOf(dir)
    }
}
