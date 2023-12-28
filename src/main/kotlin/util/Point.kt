package util

import kotlin.math.abs

data class IntPoint(val x: Int, val y: Int)

data class Point(val x: Long, val y: Long) : Comparable<Point> {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
    constructor(p: Pair<Int, Int>) : this(p.first, p.second)

    val int by lazy { IntPoint(x.toInt(), y.toInt()) }

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

    operator fun plus(o: Point) = copy(x = x + o.x, y = y + o.y)

    override fun compareTo(other: Point): Int {
        return (x + y).compareTo(other.x + other.y)
    }

    operator fun contains(o: Point): Boolean {
        return o.x in (0..<x) && o.y in (0..<y)
    }

    fun mod(r: Point) = Point(x.mod(r.x), y.mod(r.y))
}