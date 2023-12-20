package util

enum class RelativeDirection(val m: Int) {
    LEFT(+1), RIGHT(-1), AHEAD(0), BACKWARDS(0)
}

enum class CardinalDirection(val x: Int, val y: Int) {
    N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

    fun rotation(other: CardinalDirection) = when {
        right == other -> -1
        other.right == this -> +1
        else -> 0
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
}
