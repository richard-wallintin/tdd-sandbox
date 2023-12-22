package y2023.day16

import util.CardinalDirection
import util.Point

data class Contraption(val lines: List<String>) {
    val size = Point(lines.first().length, lines.size)

    fun trace(
        beam: Beam = Beam(location = Point(0, 0), direction = CardinalDirection.E)
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
        if (point.x in (0 until size.x) && point.y in (0 until size.y)) Tile(lines[point.y.toInt()][point.x.toInt()]) else null

    fun illumination(
        beam: Beam = Beam(
            location = Point(0, 0),
            direction = CardinalDirection.E
        )
    ): Int {
        val seen = mutableSetOf(beam)
        traverse(beam, tile(beam.location) ?: return 0, seen)
        return seen.map { it.location }.toSet().size
    }

    private fun traverse(beam: Beam, tile: Tile, seen: MutableSet<Beam>) {
        beam.encounter(tile).map { it.next() }.forEach { n ->
            tile(n.location)?.let { t ->
                if (seen.add(n)) {
                    traverse(n, t, seen)
                }
            }
        }
    }

    fun maxIllumination(): Int {
        val top = (0 until size.x).map { Point(it, 0) }.map { Beam(it, CardinalDirection.S) }
        val bottom =
            (0 until size.x).map { Point(it, size.y - 1) }.map { Beam(it, CardinalDirection.N) }
        val left = (0 until size.y).map { Point(0, it) }.map { Beam(it, CardinalDirection.E) }
        val right =
            (0 until size.y).map { Point(size.x - 1, it) }.map { Beam(it, CardinalDirection.W) }

        return (top + right + bottom + left).asSequence().map { illumination(it) }.max()
    }


    companion object {
        fun parse(lines: String): Contraption {
            return Contraption(lines.lines())
        }
    }

}
