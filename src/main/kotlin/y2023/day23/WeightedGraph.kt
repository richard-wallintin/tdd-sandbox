package y2023.day23

import java.util.*

data class Edge<N>(val nodes: Set<N>, val weight: Long) {
    constructor(from: N, to: N, weight: Long) : this(setOf(from, to), weight)
    constructor(from: N, to: N, weight: Int) : this(setOf(from, to), weight.toLong())

    init {
        assert(nodes.size == 2)
    }

    val from get() = nodes.first()
    val to get() = nodes.last()

    override fun toString(): String {
        return "$from <=[$weight]=> $to"
    }

    fun other(node: N) = if (from == node) to else from
    fun invert() = copy(weight = -weight)
}

data class WeightedGraph<N>(val edges: Set<Edge<N>>) {
    private val links = mutableMapOf<N, Map<N, Long>>()

    val minWeight by lazy { edges.minOf { it.weight } }
    val maxWeight by lazy { edges.maxOf { it.weight } }

    private fun links(n: N) = links.getOrPut(n) {
        edges.filter { it.nodes.contains(n) }.map {
            it.other(n) to it.weight
        }.toMap()
    }

    inner class Path(val node: N, val length: Long = 0) {
        fun next() =
            links(node).entries.map { (n, w) -> Path(node = n, length = length + w) }
    }

    fun shortestPaths(start: N, finish: N, heuristic: (N, N) -> Long) = sequence {
        val q = PriorityQueue(compareBy<Path> { it.length + heuristic(it.node, finish) })
        q.add(Path(start))
        val seen = mutableSetOf(start)

        while (q.isNotEmpty()) {
            val p = q.remove()

            if (p.node == finish) yield(p.length)

            q.addAll(p.next().filter { seen.add(it.node) })
        }
    }

    fun invert() = copy(edges = edges.map { it.invert() }.toSet())
}