package y2023.day24

import util.Point3D
import kotlin.math.roundToLong

data class Hailstone(val position: Point3D, val velocity: Point3D) {
    fun next() = copy(position = position + velocity)

    private val slope = velocity.y.toDouble() / velocity.x.toDouble()
    private val offset = position.y - slope * position.x

    fun planeCross(b: Hailstone, range: LongRange): Boolean {
        val slopeDiff = slope - b.slope
        val offsetDiff = b.offset - offset

        // parallel (or overlapping)
        if (slopeDiff == 0.0) return offsetDiff == 0.0

        val x = offsetDiff / slopeDiff
        val y = offset + slope * x

        if (isPast(x) || b.isPast(x)) return false

        return x.roundToLong() in range && y.roundToLong() in range
    }

    private fun isPast(x: Double) = if (velocity.x > 0) x < position.x else x > position.x

    companion object {
        fun of(s: String) =
            Regex("([+-]?\\d+),\\s+([+-]?\\d+),\\s+([+-]?\\d+)\\s+@\\s+([+-]?\\d+),\\s+([+-]?\\d+),\\s+([+-]?\\d+)")
                .matchEntire(s)?.destructured?.let { (px, py, pz, vx, vy, vz) ->
                    Hailstone(
                        position = Point3D(px.toLong(), py.toLong(), pz.toLong()),
                        velocity = Point3D(vx.toLong(), vy.toLong(), vz.toLong())
                    )
                } ?: throw IllegalArgumentException("no hailstone: '$s'")

        fun ofMany(text: String) = text.lineSequence().map { of(it) }

        fun <E> List<E>.allPairs(): Sequence<Pair<E, E>> = sequence {
            if (size >= 2) {
                val h = first()
                val tail = drop(1)
                tail.forEach { o -> yield(h to o) }
                yieldAll(tail.allPairs())
            }
        }

        fun Sequence<Hailstone>.intersectingHailStones(range: LongRange) =
            toList().allPairs().count { (a, b) -> a.planeCross(b, range) }
    }
}
