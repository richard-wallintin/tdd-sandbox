package y2023.day22

import util.Point3D
import util.Rectangle
import util.Rectangle.Companion.effectiveArea
import kotlin.math.max
import kotlin.math.min

data class Brick(val start: Point3D, val end: Point3D) {
    val top = max(start.z, end.z)
    val bottom = min(start.z, end.z)
    val volume = start.distance(end) + 1

    private val horizontalArea = Rectangle.ofTiles(
        start.plane, end.plane
    )

    fun horizontalOverlap(other: Brick) =
        this.horizontalArea.overlap(other.horizontalArea).effectiveArea() > 0

    fun elevate(amount: Long) = copy(
        start = start.elevate(amount),
        end = end.elevate(amount)
    )

    fun elevateTo(z: Long) = elevate(z - bottom)

    companion object {
        fun of(line: String): Brick {
            val (s, e) = line.split("~")
            return Brick(
                Point3D(s.split(",").map { it.toLong() }),
                Point3D(e.split(",").map { it.toLong() })
            )
        }

        fun ofMany(text: String) = text.lines().map { of(it) }

        fun List<Brick>.safe(): Int = BrickStack.of(this).safeCount()
        fun List<Brick>.fallPotential(): Int = BrickStack.of(this).fallPotential()
    }

    fun support(above: List<Brick>) =
        above.filter { it.bottom == top + 1 }.filter { this.horizontalOverlap(it) }
}
