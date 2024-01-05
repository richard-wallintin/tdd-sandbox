package util

import kotlin.math.abs

data class Point3D(val x: Long, val y: Long, val z: Long) {
    constructor(coordinates: List<Long>) : this(coordinates[0], coordinates[1], coordinates[2])

    val plane = Point(x, y)

    fun distance(end: Point3D) = abs(end.x - x) + abs(end.y - y) + abs(end.z - z)

    fun elevate(amount: Long) = copy(z = z + amount)

    operator fun plus(o: Point3D) = copy(
        x = x + o.x,
        y = y + o.y,
        z = z + o.z
    )

    operator fun times(f: Long) = copy(
        x = x * f,
        y = y * f,
        z = z * f
    )

    override fun toString() = "($x, $y, $z)"
}
