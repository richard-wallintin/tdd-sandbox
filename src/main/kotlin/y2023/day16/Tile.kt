package y2023.day16

import util.CardinalDirection

data class Tile(val type: Char) {
    fun divert(dir: CardinalDirection): List<CardinalDirection> {
        when (type) {
            '|' -> {
                if (dir == CardinalDirection.W || dir == CardinalDirection.E)
                    return listOf(CardinalDirection.N, CardinalDirection.S)
            }

            '-' -> {
                if (dir == CardinalDirection.N || dir == CardinalDirection.S)
                    return listOf(CardinalDirection.W, CardinalDirection.E)
            }

            '/' -> {
                return when (dir) {
                    CardinalDirection.W -> listOf(CardinalDirection.S)
                    CardinalDirection.E -> listOf(CardinalDirection.N)
                    CardinalDirection.N -> listOf(CardinalDirection.E)
                    CardinalDirection.S -> listOf(CardinalDirection.W)
                }
            }

            '\\' -> {
                return when (dir) {
                    CardinalDirection.W -> listOf(CardinalDirection.N)
                    CardinalDirection.E -> listOf(CardinalDirection.S)
                    CardinalDirection.N -> listOf(CardinalDirection.W)
                    CardinalDirection.S -> listOf(CardinalDirection.E)
                }
            }
        }
        return listOf(dir)
    }
}
