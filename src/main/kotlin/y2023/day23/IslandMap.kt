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

    data class Hike(val length: Int, val to: Point, val prev: Hike? = null) {
        fun next() = CardinalDirection.entries.map { go(it) }
        fun go(d: CardinalDirection): Hike = copy(length = length + 1, to = to.go(d), prev = this)
        fun seen(p: Point): Boolean = to == p || prev?.seen(p) ?: false
    }

    private fun check(h: Hike): Hike? = tile(h.to).let { tile ->
        if (tile.path) {
            tile.slope?.let { check(h.go(it)) } ?: h
        } else null
    }


    val hikeLength: Int by lazy { search().max() }

    private fun search() = sequence {
        val q = PriorityQueue(
            compareBy<Hike> { it.length }.reversed()
        )
        q.add(Hike(0, start))
        while (q.isNotEmpty()) {
            val hike = q.remove()
            if (hike.to == finish) yield(hike.length)
            hike.next().mapNotNull { check(it) }
                .filter { !hike.seen(it.to) }
                .forEach(q::add)
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
