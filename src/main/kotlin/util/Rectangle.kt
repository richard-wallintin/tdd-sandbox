package util

import kotlin.math.max
import kotlin.math.min

data class Rectangle(val a: Point, val c: Point) {

    init {
        assert(a.topLeft(c)) { "a=$a must be ≤ c=$c" }
    }

    fun overlap(o: Rectangle): Rectangle? {
        val newA = Point(
            max(this.a.x, o.a.x),
            max(this.a.y, o.a.y),
        )
        val newC = Point(
            min(this.c.x, o.c.x),
            min(this.c.y, o.c.y)
        )
        return if (newA.topLeft(newC)) Rectangle(newA, newC) else null
    }

    val area by lazy { (c.x - a.x) * (c.y - a.y) }

    companion object {
        fun of(m: Point, n: Point) = Rectangle(min(m, n), max(m, n))

        fun ofTiles(first: Point, last: Point) =
            Rectangle(min(first, last), max(first, last) + 1)

        private fun max(m: Point, n: Point) = Point(max(m.x, n.x), max(m.y, n.y))

        private fun min(m: Point, n: Point) = Point(min(m.x, n.x), min(m.y, n.y))

        fun Rectangle?.effectiveArea(): Long = this?.area ?: 0
    }
}