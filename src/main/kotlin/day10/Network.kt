package day10

data class Network(val matrix: List<List<PipeSegment>>) {
    val size: Point = Point(matrix.maxOf { it.size }, matrix.size)
    val startNode: Node by lazy { findStartNode() }

    private fun findStartNode(): Node {
        matrix.forEachIndexed { y, row ->
            row.forEachIndexed { x, p ->
                if (p.isStart()) return Node(Point(x, y), p)
            }
        }
        throw IllegalStateException("no start node found")
    }

    private fun nodeAt(location: Point): Node? =
        matrix.getOrNull(location.y)?.getOrNull(location.x)?.let {
            Node(location, it)
        }

    inner class Node(val location: Point, private val pipe: PipeSegment) {
        private val connect by lazy { connectNodes().toMap() }

        private fun connectNodes() = sequence {
            pipe.connects.forEach { dir ->
                val n = nodeAt(dir)
                if (n != null && n.connectsTo(dir.inverse)) yield(dir to n)
            }
        }

        private fun connectsTo(direction: Direction) = pipe.connects.contains(direction)

        private fun nodeAt(dir: Direction) = nodeAt(location.go(dir))

        val neighbours by lazy {
            Direction.values().mapNotNull { d -> nodeAt(d)?.let { d to it } }.toMap()
        }

        private fun next(direction: Direction): Node =
            connect[direction] ?: throw IllegalArgumentException(
                "cant go $direction from $location ($pipe)"
            )

        private fun nextDirectionComingFrom(direction: Direction) =
            connect.keys.first { it != direction }

        fun traverse(direction: Direction = connect.keys.min()) = sequence {
            var d = direction
            yield(this@Node to d)
            var n = next(d)
            while (n.location != this@Node.location) {
                d = n.nextDirectionComingFrom(d.inverse)
                yield(n to d)
                n = n.next(d)
            }
        }

        fun isGround() = pipe.isGround()
    }


    companion object {
        fun parse(s: String): Network {
            return Network(s.lineSequence().map { line ->
                line.asSequence().map {
                    PipeSegment.of(it.toString())
                }.toList()
            }.toList())
        }
    }

    override fun toString(): String {
        return matrix.joinToString("\n") { it.joinToString("") }
    }
}
