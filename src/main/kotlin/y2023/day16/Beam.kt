package y2023.day16

import util.CardinalDirection
import util.Point

data class Beam(val location: Point, val direction: CardinalDirection) {
    fun next() = copy(location = location.go(direction))

    fun encounter(tile: Tile) = tile.divert(direction).map {
        copy(direction = it)
    }

    companion object {
        fun Sequence<Beam>.uniqueLocations() =
            map { it.location }.toSet()
    }
}
