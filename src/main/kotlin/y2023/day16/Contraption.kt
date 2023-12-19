package y2023.day16

import util.Direction
import util.Point

data class Contraption(val lines: List<String>) {
    val size = Point(lines.first().length, lines.size)

    fun trace(
        beam: Beam = Beam(location = Point(0, 0), direction = Direction.E)
    ) = sequence {
        val seen: MutableSet<Beam> = mutableSetOf()
        yieldAll(runTrace(beam, seen))
    }

    private fun runTrace(
        beam: Beam,
        seen: MutableSet<Beam>
    ): Sequence<Beam> {
        return tile(beam.location)?.let { tile ->
            sequenceOf(beam) +
                    beam.encounter(tile)
                        .asSequence()
                        .map { it.next() }
                        .filter { seen.add(it) }
                        .flatMap { runTrace(it, seen) }
        } ?: emptySequence()
    }

    private fun tile(point: Point) =
        if (point.x in (0 until size.x) && point.y in (0 until size.y)) Tile(lines[point.y][point.x]) else null

    fun illumination(beam: Beam = Beam(location = Point(0, 0), direction = Direction.E)): Int {
        val seen = mutableSetOf(beam)
        traverse(beam, tile(beam.location) ?: return 0, seen)
        return seen.map { it.location }.toSet().size
    }

    private fun traverse(beam: Beam, tile: Tile, seen: MutableSet<Beam>) {
        beam.encounter(tile).map { it.next() }
            .forEach { nextBeam ->
                tile(nextBeam.location)?.let { t ->
                    if (seen.add(nextBeam)) {
                        traverse(nextBeam, t, seen)
                    }
                }
            }
    }


    companion object {
        fun parse(lines: String): Contraption {
            return Contraption(lines.lines())
        }
    }

}
