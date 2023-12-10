package day10

enum class Direction(val x: Int, val y: Int) {
    N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

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
}
