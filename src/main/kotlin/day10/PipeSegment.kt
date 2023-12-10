package day10

data class PipeSegment(val connects: Set<Direction>) {
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

val vertical = setOf(Direction.N, Direction.S)
val horizontal = setOf(Direction.E, Direction.W)
val cornerNE = setOf(Direction.N, Direction.E)
val cornerNW = setOf(Direction.N, Direction.W)
val cornerSW = setOf(Direction.S, Direction.W)
val cornernSE = setOf(Direction.S, Direction.E)
val ground = emptySet<Direction>()
val start = setOf(
    Direction.S,
    Direction.E,
    Direction.W,
    Direction.N
)