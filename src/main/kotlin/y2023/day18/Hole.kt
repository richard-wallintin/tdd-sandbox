package y2023.day18

import util.CardinalDirection
import util.Point
import util.RelativeDirection

data class Hole(
    val location: Point = Point(0, 0),
    val from: Hole? = null
) {

    val direction = from?.location?.direction(location)
    private val backtrack: Sequence<Hole> = sequence {
        yield(this@Hole)
        from?.let { yieldAll(it.backtrack) }
    }

    val rotation by lazy {
        backtrack.map { it.location }.zipWithNext().map { (a, b) -> b.direction(a) }
            .zipWithNext().map { (a, b) -> b.rotation(a) }
            .sum()
    }

    private val boundary by lazy { backtrack.map { it.location }.toSet() }
    val length get() = boundary.size

    val interior: Set<Point> by lazy {
        sequence {
            val inside = if (rotation > 0) RelativeDirection.LEFT else RelativeDirection.RIGHT

            backtrack.forEach { h ->
                h.direction?.let { d ->
                    h.location.axis(d.turn(inside)).takeWhile { it !in boundary }
                        .forEach { yield(it) }
                }
            }

        }.toSet()
    }

    val size get() = boundary.size + interior.size

    fun dig(instruction: Instruction): Hole {
        return List(instruction.meters) { instruction.direction }.fold(this, Hole::dig)
    }

    private fun dig(direction: CardinalDirection) = Hole(
        location = location.go(direction),
        from = this
    )

    companion object {
        fun Sequence<Instruction>.digTrench() = fold(Hole(), Hole::dig)
    }
}
