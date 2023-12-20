package util

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    fun go(dir: CardinalDirection) = Point(x + dir.x, y + dir.y)
    fun area() = sequence {
        for (h in -1..1) {
            for (v in -1..1) {
                yield(Point(x + h, y + v))
            }
        }
    }.filter { it != this }

    infix fun distance(o: Point): Int {
        return abs(x - o.x) + abs(y - o.y)
    }
}