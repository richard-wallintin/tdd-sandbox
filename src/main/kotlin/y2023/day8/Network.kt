package y2023.day8

import java.math.BigInteger

enum class Direction(val go: (Node) -> String) {
    L(Node::left), R(Node::right);

    companion object {
        fun String.navigationInstructions() =
            toList().map(Char::toString).map(Direction::valueOf)
    }
}

fun lcm(a: BigInteger, b: BigInteger?): BigInteger {
    val gcd = a.gcd(b)
    val absProduct = a.multiply(b).abs()
    return absProduct.divide(gcd)
}

typealias NodeSelector = (Node) -> Boolean

class Network(val nodes: Map<String, Node>) {
    constructor(nodes: List<Node>) : this(nodes.associateBy { it.id })

    fun navigate(startNodeId: String, direction: Direction): Node {
        return move(startNodeId.node(), direction)
    }

    fun navigate(startNodeId: String, directions: List<Direction>): Node {
        return directions.fold(startNodeId.node(), ::move)
    }

    private fun move(n: Node, d: Direction) = n.let(d.go).node()
    private fun String.node() = let(nodes::getValue)
    private fun NodeSelector.nodes() = nodes.values.filter(this)

    private fun move(nodes: List<Node>, d: Direction) = nodes.map { move(it, d) }

    fun navigate(startNodes: NodeSelector, directions: List<Direction>): List<Node> {
        return directions.fold(startNodes.nodes(), ::move)
    }

    fun find(startNodeId: String, endNodeId: String, directions: List<Direction>): Long {
        return find(startNodeId, { it.id == endNodeId }, directions)
    }

    fun find(startNodeId: String, endNodes: NodeSelector, directions: List<Direction>): Long {
        return find(startNodeId.node(), endNodes, directions)
    }

    private fun find(
        node: Node,
        endNodes: NodeSelector,
        directions: List<Direction>
    ): Long {
        directions.foreverRepeating().withIndex()
            .fold(node) { currentNode, step ->
                if (step.index > 0 && endNodes(currentNode)) return@find step.index.toLong()
                else move(currentNode, step.value)
            }.let {
                throw IllegalStateException("result after infinite folding: $it")
            }
    }

    fun find(
        startNodes: NodeSelector,
        endNodes: NodeSelector,
        directions: List<Direction>
    ): BigInteger {
        val initial = startNodes.nodes()

        return initial.map {
            find(it, endNodes, directions)
        }.map(BigInteger::valueOf).reduce(::lcm)
    }
}


private fun <E> List<E>.foreverRepeating() = sequence {
    while (true) {
        val iterator = iterator()
        while (iterator.hasNext()) yield(iterator.next())
    }
}
