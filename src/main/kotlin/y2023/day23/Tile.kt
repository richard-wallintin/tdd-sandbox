package y2023.day23

import util.CardinalDirection

data class Tile(val path: Boolean, val slope: CardinalDirection? = null) {
    companion object {
        fun of(c: Char) = when (c) {
            '.' -> Tile(path = true)
            '#' -> Tile(path = false)
            '>' -> Tile(path = true, slope = CardinalDirection.E)
            'v' -> Tile(path = true, slope = CardinalDirection.S)
            '<' -> Tile(path = true, slope = CardinalDirection.W)
            '^' -> Tile(path = true, slope = CardinalDirection.N)
            else -> throw IllegalArgumentException("unknown tile '$c'")
        }
    }

    override fun toString() =
        if (path) "." else "#"
}
