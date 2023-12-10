package day10

typealias Point = Pair<Int, Int>

data class Network(val matrix: List<List<PipeSegment>>) {
    val size: Pair<Int, Int> = matrix.maxOf { it.size } to matrix.size
    val startNode: Node by lazy {
        findStartNode()
    }

    private fun findStartNode(): Node {
        matrix.forEachIndexed { y, row ->
            row.forEachIndexed { x, p ->
                if (p.isStart()) return Node(x to y, p)
            }
        }
        throw IllegalStateException("no start node found")
    }

    private fun nodeAt(location: Point): Node? =
        matrix.getOrNull(location.second)?.getOrNull(location.first)?.let {
            Node(location, it)
        }


    inner class Node(val location: Point, val pipe: PipeSegment) {
        private val connect by lazy { connectNodes().toMap() }

        fun next(direction: Direction): Node = connect[direction] ?: throw IllegalArgumentException(
            "cant go $direction from $location ($pipe)"
        )

        private fun nextDirectionComingFrom(direction: Direction) =
            connect.keys.first { it != direction }

        private fun connectNodes() = sequence {
            pipe.connects.forEach { dir ->
                val n = nodeAt((location.first + dir.x) to (location.second + dir.y))
                if (n != null && n.pipe.connects.contains(dir.inverse)) yield(dir to n)
            }
        }

        fun traverse(direction: Direction = connect.keys.min()) = sequence {
            yield(this@Node)
            var d = direction
            var n = next(d)
            while (n.location != this@Node.location) {
                yield(n)
                d = n.nextDirectionComingFrom(d.inverse)
                n = n.next(d)
            }
        }
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
