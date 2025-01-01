package y2024.day16

import util.CardinalDirection
import util.Point
import util.RelativeDirection

data class Placement(val location: Point, val direction: CardinalDirection) {
    fun move() = copy(location = location.go(direction))
    fun rotate(r: RelativeDirection) = copy(direction = direction.turn(r))

    companion object {
        infix fun Point.facing(direction: CardinalDirection) = Placement(this, direction)
    }
}