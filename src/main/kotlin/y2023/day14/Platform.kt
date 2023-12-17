package y2023.day14

import util.Direction
import y2023.day14.Lane.Companion.invert
import y2023.day14.Lane.Companion.transpose

data class Platform(val lanes: List<Lane>, val orientation: Direction = Direction.W) {
    fun turn(d: Direction): Platform = Platform(
        when {
            orientation.right == d -> lanes.transpose().reversed()
            orientation.left == d -> lanes.reversed().transpose()
            orientation.inverse == d -> lanes.invert().reversed()
            else -> lanes
        },
        orientation = d
    )

    private val load by lazy { lanes.sumOf { it.load } }

    fun load(d: Direction): Int {
        return turn(d).load
    }

    private fun tilt(): Platform = copy(
        lanes = lanes.map { it.tilt() }
    )

    fun tilt(d: Direction): Platform {
        return turn(d).tilt().turn(orientation)
    }

    fun tiltCycle(count: Int): Platform {
        val seen = mutableMapOf<Platform, Int>()
        val lookup = mutableListOf<Platform>()

        var result = this
        repeat(count) { round ->
            result = result.tiltCycle()

            seen[result]?.let { firstOccurrence ->
                val period = round - firstOccurrence
                return lookup[
                    (count - 1).minus(firstOccurrence).mod(period).plus(firstOccurrence)
                ]
            }

            seen[result] = round
            lookup.add(result)
        }
        return result
    }

    fun tiltCycle() = listOf(Direction.N, Direction.W, Direction.S, Direction.E)
        .fold(this) { p, d -> p.turn(d).tilt() }.turn(orientation)


    override fun toString(): String {
        return lanes.joinToString("\n")
    }

    companion object {
        fun of(text: String, d: Direction = Direction.W) = Platform(
            text.lines().map(::Lane), d
        )
    }
}
