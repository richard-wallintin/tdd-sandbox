package y2024.day12

import util.CardinalDirection
import util.Point
import util.groupMatches

data class Region(val points: Set<Point>) {

    val area: Int = points.size

    val fences: Sequence<Fence> = points.asSequence().flatMap { p ->
        CardinalDirection.entries.filter {
            p.go(it) !in points
        }.map { Fence(p, it) }
    }

    val perimeter: Int by lazy(fences::count)

    val price get() = area * perimeter
    val sides: Int by lazy { fences.toSet().groupMatches(Fence::extends).size }

    val discountedPrice get()= area * sides

    data class Fence(val p: Point, val d: CardinalDirection) {
        fun extends(fence: Fence): Boolean {
            return this.d == fence.d && when (d) {
                CardinalDirection.N, CardinalDirection.S ->
                    this.p.horizontalNeighbour(fence.p)

                CardinalDirection.W, CardinalDirection.E ->
                    this.p.verticalNeighbour(fence.p)
            }
        }
    }
}

