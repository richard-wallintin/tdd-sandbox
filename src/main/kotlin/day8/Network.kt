package day8

enum class Direction(val go: (Node) -> String) {
    L(Node::left), R(Node::right);

    companion object {
        fun String.navigationInstructions() =
            toList().map(Char::toString).map(Direction::valueOf)
    }
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

    fun find(startNodeId: String, endNodeId: String, directions: List<Direction>): Int {
        directions.foreverRepeating().withIndex()
            .fold(startNodeId.node()) { currentNode, step ->
                if (currentNode.id == endNodeId) return@find step.index
                else move(currentNode, step.value)
            }.let {
                throw IllegalStateException("result after infinite folding: $it")
            }
    }

    fun navigate(startNodes: NodeSelector, directions: List<Direction>): List<Node> {
        return directions.fold(startNodes.nodes(), ::move)
    }

    private fun move(nodes: List<Node>, d: Direction) = nodes.map { move(it, d) }

    fun find(startNodes: NodeSelector, endNodes: NodeSelector, directions: List<Direction>): Long {
        var count = 0L
        directions.foreverRepeating()
            .fold(startNodes.nodes()) { currentNodes, dir ->
                if (currentNodes.all(endNodes)) return@find count
                else {
                    count++
                    if(count.mod(1_000_000_000L) == 0L){
                        println("${count.div(1_000_000_000)} * 10^9 @ ${System.currentTimeMillis()}")
                    }
                    move(currentNodes, dir)
                }
            }.let {
                throw IllegalStateException("result after infinite folding: $it")
            }
    }

}


private fun <E> List<E>.foreverRepeating() = sequence {
    while (true) {
        val iterator = iterator()
        while (iterator.hasNext()) yield(iterator.next())
    }
}
