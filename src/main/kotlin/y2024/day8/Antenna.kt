package y2024.day8

import util.Point
import util.Rectangle
import util.forever

data class Antenna(val location: Point, val frequency: Char) {
    fun antinodes(next: Antenna, boundary: Rectangle, enhanced: Boolean = false): List<Point> {
        if (frequency != next.frequency) return emptyList()
        val vector = next.location - location

        return if (enhanced) {
            (next.location.forever { this + vector }.takeWhile { it in boundary } +
                    location.forever { this - vector }.takeWhile { it in boundary }).toList()
        } else {
            listOf(next.location + vector, location - vector).filter { it in boundary }
        }
    }
}
