package y2023.day18

import util.CardinalDirection
import util.Point
import kotlin.math.abs

data class Instruction(val direction: CardinalDirection, val units: Long) {
    companion object {
        fun of(s: String, hex: Boolean = false): Instruction {
            return Regex("(\\w) (\\d+) \\(#(\\w+)\\)").matchEntire(s)?.destructured?.let { (dir, meters, hexCode) ->
                if (hex) {
                    Instruction(
                        direction = directionFromHex(hexCode.last()),
                        units = hexCode.substring(0, 5).toLong(16)
                    )
                } else
                    Instruction(
                        direction = CardinalDirection.of(dir),
                        units = meters.toLong()
                    )
            } ?: throw IllegalArgumentException("no instruction '$s'")
        }

        private fun directionFromHex(c: Char): CardinalDirection {
            return when (c) {
                '0' -> CardinalDirection.E
                '1' -> CardinalDirection.S
                '2' -> CardinalDirection.W
                '3' -> CardinalDirection.N
                else -> throw IllegalArgumentException("unknown hex direction $c")
            }
        }

        fun ofMany(lines: String, hex: Boolean = false) = lines.lineSequence().map { of(it, hex) }

        fun Iterable<Instruction>.toPolygon() =
            runningFold(Point(0, 0)) { p, i ->
                p.go(i.direction, i.units)
            }

        fun List<Point>.area(): Long {
            return abs(zipWithNext { a, b ->
                (a.x * b.y) - (a.y * b.x)
            }.sum() / 2)
        }

        fun List<Instruction>.computeArea(): Long {
            val polygon = toPolygon()
            val polygonArea = polygon.area()
            val boundary = sumOf { it.units }
            return polygonArea + 1 + boundary / 2
        }
    }
}