package y2024.day8

import util.Grid
import util.Point
import util.Rectangle
import util.pick

data class AntennaMap(
    val antennas: Set<Antenna> = emptySet(),
    val size: Point,
    val enhanced: Boolean = false
) {
    val boundary = Rectangle(Point(0, 0), size)

    val antennaCount: Int = antennas.size

    val antinodeCount: Int by lazy {
        antennas.toList().pick(2).map { (x, y) ->
            x.antinodes(y, boundary, enhanced).toSet()
        }.reduce(Set<Point>::plus).size
    }

    fun enhanced(): AntennaMap {
        return copy(enhanced = true)
    }

    companion object {
        fun parse(text: String): AntennaMap {
            val grid = text.lines().map(String::toList).let(::Grid)

            return AntennaMap(
                grid.findAll { it != '.' }.map { (loc, freq) -> Antenna(loc, freq) }.toSet(),
                grid.size
            )
        }
    }

}
