package y2024.day12

import util.Point

data class Region(val points: Set<Point>) {
    val area: Int = points.size
    val perimeter: Int by lazy {
        points.sumOf {
            it.cardinalEnv().count { it !in points }
        }
    }
    val price get() = area * perimeter
}
