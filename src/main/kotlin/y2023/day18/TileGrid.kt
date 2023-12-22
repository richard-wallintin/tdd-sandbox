package y2023.day18

import util.CardinalDirection
import util.CardinalDirection.*
import util.Point
import y2023.day18.Hole.Companion.digTrench
import kotlin.math.abs

data class Segment(
    val instruction: Instruction,
    val from: Point,
    val to: Point
) {
    val length = from distance to
    val direction = from direction to
}

data class TileGrid(
    val segments: List<Segment>,
    val xAxis: Map<Int, Int>,
    val yAxis: Map<Int, Int>
) {
    val origin = segments.first().from

    val size = Point(xAxis.size, yAxis.size)
    private val xAxisReversed = invert(xAxis)
    private val yAxisReversed = invert(yAxis)


    val mappedInstructions by lazy {
        segments.map {
            Instruction(
                direction = it.instruction.direction,
                units = mapToTile(it.from) distance mapToTile(it.to)
            )
        }
    }

    private fun mapToTile(originalPoint: Point) =
        Point(xAxis[originalPoint.x]!!, yAxis[originalPoint.y]!!)


    private fun mapToOriginal(tile: Point) = xAxisReversed[tile.x]?.let { ox ->
        yAxisReversed[tile.y]?.let { oy ->
            Point(ox, oy)
        }
    } ?: throw IllegalArgumentException("tile cannot be mapped: $tile")

    private fun invert(map: Map<Int, Int>) = map.entries.associate { (k, v) -> v to k }

    fun fullSizeOf(tile: Point): Int {
        return xSizeOf(tile) * ySizeOf(tile)
    }

    private fun xSizeOf(tile: Point) =
        xAxisReversed[tile.x + 1]?.let { abs(it - xAxisReversed[tile.x]!!) }
            ?: throw IllegalArgumentException("?? tile")

    private fun ySizeOf(tile: Point) =
        yAxisReversed[tile.y + 1]?.let { abs(it - yAxisReversed[tile.y]!!) }
            ?: throw IllegalArgumentException("?? tile")

    private fun boundarySizeOf(
        enter: CardinalDirection,
        leave: CardinalDirection,
        p: Point
    ) = when (val it = enter to leave) {
        W to W -> xSizeOf(p)
        W to N -> xSizeOf(p)
        E to S -> ySizeOf(p)
        S to S -> ySizeOf(p)
        S to W -> 1
        W to S -> xSizeOf(p) + ySizeOf(p) - 1
        else -> fullSizeOf(p)
    }

    private val trench by lazy {
        mappedInstructions.asSequence().digTrench(
            mapToTile(origin)
        )
    }

    fun trenchSize(): Int {
        val interiorSize = trench.interior.sumOf { fullSizeOf(it) }
        val trenchArea = computeTrenchArea()
        return interiorSize + trenchArea
    }

    private fun computeTrenchArea(): Int {
        return trench.loop().sumOf { (enter, p, leave) ->
            boundarySizeOf(enter, leave, p)
        }
    }

    companion object {
        fun from(instructions: Sequence<Instruction>): TileGrid {
            val originalInstructions = instructions.toList()
            val points = originalInstructions.runningFold(Point(0, 0)) { p, i ->
                p.go(i.direction, i.units)
            }
            assert(originalInstructions.asSequence().map { it.direction }
                .zipWithNext(CardinalDirection::rotation)
                .sum() < 0) { "assuming right turning trench" }

            return TileGrid(
                points.zipWithNext().zip(originalInstructions).map { (p, i) ->
                    Segment(i, p.first, p.second)
                },
                gridMapping(points.map { it.x }),
                gridMapping(points.map { it.y })
            )
        }

        private fun gridMapping(values: List<Int>) =
            values.toSortedSet().withIndex().associate { it.value to it.index }
    }

}
