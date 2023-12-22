package util

import kotlin.math.abs

data class Point(val x: Long, val y: Long) {
    constructor(x: Int, y: Int): this(x.toLong(), y.toLong())

    fun go(dir: CardinalDirection, dist: Long = 1) = Point(x + dir.x * dist, y + dir.y * dist)
    fun env() = sequence {
        for (h in -1..1) {
            for (v in -1..1) {
                yield(Point(x + h, y + v))
            }
        }
    }.filter { it != this }

    infix fun distance(o: Point): Long {
        return abs(x - o.x) + abs(y - o.y)
    }

    infix fun direction(o: Point) = when {
        o.x > this.x -> CardinalDirection.E
        o.x < this.x -> CardinalDirection.W
        o.y > this.y -> CardinalDirection.S
        o.y < this.y -> CardinalDirection.N
        else -> throw IllegalArgumentException("no clear direction")
    }

    fun axis(d: CardinalDirection) = sequence {
        var p = this@Point
        while (true) {
            p = p.go(d)
            yield(p)
        }
    }

    infix fun area(b: Point) = abs(b.x - x) * abs(b.y - y)
}