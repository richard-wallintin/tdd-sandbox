package y2023.day10

import util.CardinalDirection

data class PipeSegment(val connects: Set<CardinalDirection>) {
    companion object {
        fun of(s: String): PipeSegment {
            return when (s) {
                "|" -> PipeSegment(vertical)
                "-" -> PipeSegment(horizontal)
                "L" -> PipeSegment(cornerNE)
                "7" -> PipeSegment(cornerSW)
                "J" -> PipeSegment(cornerNW)
                "F" -> PipeSegment(cornernSE)
                "S" -> PipeSegment(start)
                "." -> PipeSegment(ground)
                else -> throw IllegalArgumentException("unknown pipe segment $s")
            }
        }
    }

    override fun toString(): String {
        return when (connects) {
            horizontal -> "━"
            vertical -> "┃"
            cornernSE -> "┏"
            cornerSW -> "┓"
            cornerNE -> "┗"
            cornerNW -> "┛"
            ground -> " "
            start -> "╋"
            else -> "?"
        }
    }

    fun isStart() = connects == start
}

val vertical = setOf(CardinalDirection.N, CardinalDirection.S)
val horizontal = setOf(CardinalDirection.E, CardinalDirection.W)
val cornerNE = setOf(CardinalDirection.N, CardinalDirection.E)
val cornerNW = setOf(CardinalDirection.N, CardinalDirection.W)
val cornerSW = setOf(CardinalDirection.S, CardinalDirection.W)
val cornernSE = setOf(CardinalDirection.S, CardinalDirection.E)
val ground = emptySet<CardinalDirection>()
val start = setOf(
    CardinalDirection.S,
    CardinalDirection.E,
    CardinalDirection.W,
    CardinalDirection.N
)