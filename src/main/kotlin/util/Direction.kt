package util

enum class Direction(val x: Int, val y: Int) {
    N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

    fun rotation(other: Direction) = when {
        right == other -> -1
        other.right == this -> +1
        else -> 0
    }

    val inverse: Direction by lazy {
        when (this) {
            N -> S
            E -> W
            S -> N
            W -> E
        }
    }

    val right: Direction by lazy {
        when (this) {
            N -> E
            E -> S
            S -> W
            W -> N
        }
    }

    val left: Direction get() = inverse.right
}
