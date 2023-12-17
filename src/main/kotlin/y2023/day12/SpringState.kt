package y2023.day12

enum class SpringState {
    OPERATIONAL,
    DAMAGED,
    UNKNOWN;

    companion object {
        fun Char.toState() = when (this) {
            '.' -> OPERATIONAL
            '#' -> DAMAGED
            '?' -> UNKNOWN
            else -> throw IllegalArgumentException("unknown state '${this}'")
        }
    }
}
