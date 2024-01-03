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

    fun other(node: N) =
        if (from == node) to
        else if (to == node) from
        else throw IllegalArgumentException("$node not in $this")
}

data class WeightedGraph<N>(val edges: Set<Edge<N>>) {
    private val links = mutableMapOf<N, List<Pair<N, Long>>>()

    private fun links(n: N) = links.getOrPut(n) {
        edges.filter { it.nodes.contains(n) }.map {
            it.other(n) to it.weight
        }
    }

    inner class Path(val node: N, val length: Long = 0) {
        fun next() =
            links(node).map { (n, w) -> Path(node = n, length = length + w) }
    }

    fun shortestPath(start: N, finish: N, heuristic: (N, N) -> Long): Long {
        val q = PriorityQueue(compareBy<Path> { it.length + heuristic(it.node, finish) })
        q.add(Path(start))

        while (q.isNotEmpty()) {
            val p = q.remove()

            if (p.node == finish) return p.length

            q.addAll(p.next())
        }

        throw IllegalStateException("did not find a path from $start to $finish")
    }

    fun longestPath(start: N, finish: N, visited: Set<N> = emptySet()): Long? {
        if (start == finish) return 0
        val newVisited = visited + start
        return links(start).mapNotNull { (next, weight) ->
            if (visited.contains(next)) null
            else longestPath(next, finish, newVisited)?.plus(weight)
        }.maxOrNull()
    }
}