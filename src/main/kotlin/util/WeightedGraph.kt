package util

import java.util.*

data class Edge<N>(val nodes: Set<N>, val weight: Long = 1) {
    constructor(from: N, to: N, weight: Int = 1) : this(setOf(from, to), weight.toLong())

    init {
        assert(nodes.size == 2)
    }

    val from = nodes.first()
    val to = nodes.last()

    override fun toString() = "($from <-[$weight]-> $to)"

    fun other(node: N) =
        if (from == node) to
        else if (to == node) from
        else throw IllegalArgumentException("$node not in $this")
}

data class Link<N>(val to: N, val via: Edge<N>)

data class WeightedGraph<N>(val edges: Set<Edge<N>>) {

    val nodes by lazy { edges.flatMap { it.nodes }.toSet() }

    private val links = mutableMapOf<N, List<Link<N>>>()
    private fun links(n: N) = links.getOrPut(n) {
        edges.filter { it.nodes.contains(n) }.map {
            Link(to = it.other(n), via = it)
        }
    }

    inner class Path(val node: N, val length: Long = 0, val via: Pair<Edge<N>, Path>? = null) {
        fun next() =
            links(node).map { (n, via) ->
                Path(
                    node = n,
                    length = length + via.weight,
                    via = (via to this)
                )
            }

        val edges: Sequence<Edge<N>> = sequence {
            via?.let { (e, p) ->
                yield(e)
                yieldAll(p.edges)
            }
        }
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
        return links(start).mapNotNull { (next, via) ->
            if (visited.contains(next)) null
            else longestPath(next, finish, newVisited)?.plus(via.weight)
        }.maxOrNull()
    }

    fun removeEdges(remove: Set<Edge<N>>) = copy(edges = edges - remove)

    fun findSplit(): Sequence<WeightedGraph<N>> =
        bruteForceFindSplit(edges.toList())

    private fun bruteForceFindSplit(candidates: List<Edge<N>>) = candidates.pick(3)
        .filter { this.distinct(it) }
        .map { removeEdges(it.toSet()) }
        .filter { it.groups.size > 1 }


    private fun distinct(edges: List<Edge<N>>) =
        edges.flatMap { it.nodes }.toSet().size == edges.size * 2

    val groups by lazy {
        edges.fold(emptySet(), ::mergeGroups)
    }

    private fun mergeGroups(groups: Set<Set<N>>, edge: Edge<N>): Set<Set<N>> {
        val groupA = groups.firstOrNull { edge.from in it } ?: setOf(edge.from)
        val groupB = groups.firstOrNull { edge.to in it } ?: setOf(edge.to)
        val remainingGroups = groups - setOf(groupA, groupB)
        val newJointGroup = groupA + groupB
        return remainingGroups + setOf(newJointGroup)
    }

    fun edgeFlow(n: N): Map<Edge<N>, Int> {
        val q: Queue<Path> = LinkedList()
        q.add(Path(n))

        val seen = mutableSetOf(n)
        val counter = mutableMapOf<Edge<N>, Int>()

        while (q.isNotEmpty()) {
            val p = q.remove()

            p.next()
                .filter { seen.add(it.node) }
                .forEach {
                    it.edges.forEach { edge ->
                        counter.merge(edge, 1, Int::plus)
                    }
                    q.add(it)
                }

        }

        return counter
    }

    fun edgeFlow(fractionOfNodes: Int = 2) =
        nodes.asSequence().take(nodes.size / fractionOfNodes).map { edgeFlow(it) }
            .mergeMaps(Int::plus)

    val splitWeight by lazy {
        groups.map { it.size }.reduce(Int::times)
    }
}

