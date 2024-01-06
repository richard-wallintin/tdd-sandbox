package y2023.day23

import util.*
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

    data class Hike(
        val length: Int = 0,
        val to: Point, val tile: Tile,
        val prev: Hike? = null
    ) {
        private fun directions(ignoreSlopes: Boolean) =
            if (ignoreSlopes) CardinalDirection.entries
            else tile.slope?.let { listOf(it) } ?: CardinalDirection.entries

        private fun immediateNext(lookup: (Point) -> Tile, ignoreSlopes: Boolean) =
            directions(ignoreSlopes).map { go(it, lookup) }
                .filter { it.tile.path }
                .filter { this.prev?.let { preLoc -> preLoc.to != it.to } ?: true }

        private fun continueStraight(lookup: (Point) -> Tile, ignoreSlopes: Boolean): Hike {
            val n = immediateNext(lookup, ignoreSlopes)
            return if (n.size == 1) n.first().continueStraight(lookup, ignoreSlopes) else this
        }

        fun next(lookup: (Point) -> Tile, ignoreSlopes: Boolean): List<Hike> {
            return immediateNext(lookup, ignoreSlopes)
                .map { it.continueStraight(lookup, ignoreSlopes) }
                .filter { !(it.prev?.seen(it.to) ?: false) }
        }

        fun go(d: CardinalDirection, lookup: (Point) -> Tile): Hike = to.go(d).let {
            copy(length = length + 1, to = it, tile = lookup(it), prev = this)
        }

        fun seen(p: Point): Boolean = to == p || prev?.seen(p) ?: false
        fun distance(p: Point) = to.distance(p)
    }

    val longestHikeLength: Int by lazy { search().first() }
    val longestHikeLengthIgnoringSlopes: Int by lazy {
        toGraph(ignoreSlopes = true).longestPath(start, finish)?.toInt()
            ?: throw IllegalStateException("no path from start to finish?")
    }

    private fun search() = sequence {
        val q = PriorityQueue(
            compareBy<Hike> { it.distance(finish) }.reversed()
                    then compareBy<Hike> { it.length }.reversed()
        )
        q.add(Hike(0, to = start, tile = tile(start)))

        while (q.isNotEmpty()) {
            val hike = q.remove()

            if (hike.to == finish) yield(hike.length)

            q.addAll(hike.next(::tile, false))
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

    fun toGraph(ignoreSlopes: Boolean): WeightedGraph<Point> {

        return WeightedGraph(edges(ignoreSlopes))
    }

    private fun edges(ignoreSlopes: Boolean): Set<Edge<Point>> {
        val q: Queue<Hike> = LinkedList()
        q.offer(Hike(to = start, tile = tile(start)))
        val seen = mutableSetOf<Edge<Point>>()
        while (q.isNotEmpty()) {
            val h = q.remove()

            h.next(::tile, ignoreSlopes).filter {
                seen.add(Edge(h.to, it.to, it.length - h.length))
            }.forEach(q::add)

        }
        return seen
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
