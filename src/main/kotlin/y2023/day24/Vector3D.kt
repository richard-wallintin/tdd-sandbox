package y2023.day24

data class Vector3D(val x: Long, val y: Long, val z: Long) {
    operator fun plus(o: Vector3D) = copy(
        x = x + o.x,
        y = y + o.y,
        z = z + o.z
    )

    override fun toString() = "($x, $y, $z)"
}
