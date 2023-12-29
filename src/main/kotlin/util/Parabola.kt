package util

import kotlin.math.pow
import kotlin.math.roundToLong

data class Parabola(val a: Double = 1.0, val b: Double = 0.0, val c: Double = 0.0) :
        (Long) -> Long {
    constructor(a: Long = 1, b: Long = 0, c: Long = 0) : this(
        a.toDouble(),
        b.toDouble(),
        c.toDouble()
    )

    companion object {
        fun of(p1: Point, p2: Point, p3: Point): Parabola {
            val d = (p1.x - p2.x) * (p1.x - p3.x) * (p2.x - p3.x)

            val a =
                (p3.x * (p2.y - p1.y) + p2.x * (p1.y - p3.y) + p1.x * (p3.y - p2.y)).toDouble() / d
            val b = (p3.x * p3.x * (p1.y - p2.y) + p2.x * p2.x * (p3.y - p1.y) +
                    p1.x * p1.x * (p2.y - p3.y)).toDouble() / d
            val c = (p2.x * p3.x * (p2.x - p3.x) * p1.y +
                    p3.x * p1.x * (p3.x - p1.x) * p2.y +
                    p1.x * p2.x * (p1.x - p2.x) * p3.y).toDouble() / d
            return Parabola(a, if (b == -0.0) 0.0 else b, if (c == -0.0) 0.0 else c)
        }
    }

    override fun invoke(x: Long) = (a * x * x + b * x + c).roundToLong()
    fun compute(x: Double) = a * x.pow(2) + b * x + c
}
