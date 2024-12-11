package y2024.day10

import util.Point

data class Trail(val path: List<Point>) {
    constructor(start: Point) : this(listOf(start))

    operator fun plus(next: Point) = copy(path = path + next)

    val end = path.last()
}