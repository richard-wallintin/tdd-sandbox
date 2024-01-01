package y2023.day23

import util.CardinalDirection
import util.Point
import util.transpose
import java.util.*


data class IslandMap(val tiles: List<List<Tile>>) {
    val size = tiles.size

    init {
        assert(tiles.all { size == it.size })
    }

    val start = (0..<size).map { Point(it, 0) }.first { tile(it).path }

    val finish = (0..<size).map { Point(it, size - 1) }.first { tile(it).path }
    fun tile(point: Point): Tile {
        return tiles.getOrNull(point.int.x)?.getOrNull(point.int.y) ?: Tile(path = false)
    }

    data class Hike(val length: Int, val to: Point, val tile: Tile, val prev: Hike? = null) {
        private fun directions(ignoreSlopes: Boolean) =
            if (ignoreSlopes) CardinalDirection.entries
            else tile.slope?.let { listOf(it) } ?: CardinalDirection.entries

        private fun immediateNext(lookup: (Point) -> Tile, ignoreSlopes: Boolean) =
            directions(ignoreSlopes).map { go(it, lookup) }
                .filter { it.tile.path }
                .filter { !this.seen(it.to) }

        private fun straight(lookup: (Point) -> Tile, ignoreSlopes: Boolean): Hike {
            val n = immediateNext(lookup, ignoreSlopes)
            return if (n.size == 1) n.first().straight(lookup, ignoreSlopes) else this
        }

        fun next(lookup: (Point) -> Tile, ignoreSlopes: Boolean) =
            immediateNext(lookup, ignoreSlopes).map { it.straight(lookup, ignoreSlopes) }

        fun go(d: CardinalDirection, lookup: (Point) -> Tile): Hike = to.go(d).let {
            copy(length = length + 1, to = it, tile = lookup(it), prev = this)
        }

        fun seen(p: Point): Boolean = to == p || prev?.seen(p) ?: false
        fun distance(p: Point) = to.distance(p)
    }

    val longestHikeLength: Int by lazy { search(ignoreSlopes = false).first() }
    val longestHikeLengthIgnoringSlopes: Int by lazy { search(ignoreSlopes = true).max() }

    private fun search(ignoreSlopes: Boolean) = sequence {
        val q = PriorityQueue(
            compareBy<Hike> { it.distance(finish) }.then(compareBy { it.length }).reversed()
        )
        q.add(Hike(0, start, tile(start)))

        while (q.isNotEmpty()) {
            val hike = q.remove()


            if (hike.to == finish) {
                println("===> RESULT")
                visualize(hike)
                yield(hike.length)
            }

            hike.next(::tile, ignoreSlopes).forEach(q::add)
        }
    }

    private fun visualize(h: Hike) {
        println("Hike: ${h.length}")
        (0..<size).forEach { y ->
            (0..<size).joinToString("") { x ->
                val p = Point(x, y)
                if (h.seen(p)) "O" else tile(p).toString()
            }.let(::println)
        }
    }

    companion object {
        fun of(text: String): IslandMap {
            return IslandMap(
                tiles = text.lineSequence().map { line ->
                    line.map { Tile.of(it) }
                }.toList().transpose()
            )
        }
    }

}
