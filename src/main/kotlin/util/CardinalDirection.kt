package util

enum class RelativeDirection(val rotation: Int) {
    LEFT(+1), RIGHT(-1), AHEAD(0), BACKWARDS(0)
}

enum class CardinalDirection(val x: Int, val y: Int) {
    N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

    fun rotation(other: CardinalDirection) = relative(other).rotation

    fun relative(other: CardinalDirection) = when {
        right == other -> RelativeDirection.RIGHT
        left == other -> RelativeDirection.LEFT
        inverse == other -> RelativeDirection.BACKWARDS
        else -> RelativeDirection.AHEAD
    }

    val inverse: CardinalDirection by lazy {
        when (this) {
            N -> S
            E -> W
            S -> N
            W -> E
        }
    }

    val right: CardinalDirection by lazy {
        when (this) {
            N -> E
            E -> S
            S -> W
            W -> N
        }
    }

    val left: CardinalDirection get() = inverse.right

    fun turn(d: RelativeDirection) = when (d) {
        RelativeDirection.AHEAD -> this
        RelativeDirection.LEFT -> left
        RelativeDirection.RIGHT -> right
        RelativeDirection.BACKWARDS -> inverse
    }

    companion object {
        fun of(s: String): CardinalDirection {
            return when (s) {
                "N", "U" -> N
                "W", "L" -> W
                "S", "D" -> S
                "E", "R" -> E
                else -> throw IllegalArgumentException("unknown direction $s")
            }
        }
    }
}
