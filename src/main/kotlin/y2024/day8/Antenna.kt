package y2024.day8

import util.Point

data class Antenna(val location: Point, val frequency: Char) {
    fun antinodes(antenna: Antenna): Set<Point> {
        if (frequency != antenna.frequency) return emptySet()
        val vector = antenna.location - location
        return setOf(antenna.location + vector, location - vector)
    }
}
