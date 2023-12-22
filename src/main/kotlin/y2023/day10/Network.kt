package y2023.day10

import util.CardinalDirection
import util.Point

data class Network(val matrix: List<List<PipeSegment>>) {
    val size: Point = Point(matrix.maxOf { it.size }, matrix.size)
    val mainLoopStart: Node by lazy { findStartNode() }

    private val nodes = mutableMapOf<Point, Node>()

    private fun findStartNode(): Node {
        matrix.forEachIndexed { y, row ->
            row.forEachIndexed { x, p ->
                if (p.isStart()) return Node(Point(x, y), p)
            }
        }
        throw IllegalStateException("no start node found")
    }

    private fun nodeAt(location: Point): Node? =
        matrix.getOrNull(location.y.toInt())?.getOrNull(location.x.toInt())?.let { p ->
            nodes.computeIfAbsent(location) { Node(it, p) }
        }

    inner class Node(val location: Point, private val pipe: PipeSegment) {
        private val connect by lazy { connectNodes().toMap() }

        private fun connectNodes() = sequence {
            pipe.connects.forEach { dir ->
                val n = nodeAt(dir)
                if (n != null && n.connectsTo(dir.inverse)) yield(dir to n)
            }
        }

        private fun connectsTo(direction: CardinalDirection) = pipe.connects.contains(direction)

        fun nodeAt(dir: CardinalDirection) = nodeAt(location.go(dir))

        val neighbours by lazy {
            location.env().mapNotNull { nodeAt(it) }.toList()
        }

        private fun next(direction: CardinalDirection): Node =
            connect[direction] ?: throw IllegalArgumentException(
                "cant go $direction from $location ($pipe)"
            )

        private fun nextDirectionComingFrom(direction: CardinalDirection) =
            connect.keys.first { it != direction }

        fun traverse(direction: CardinalDirection = connect.keys.min()) = sequence {
            var d = direction
            yield(this@Node to d)
            var n = next(d)
            while (n.location != this@Node.location) {
                d = n.nextDirectionComingFrom(d.inverse)
                yield(n to d)
                n = n.next(d)
            }
        }

        fun overallRotation() =
            traverse().map { it.second }.zipWithNext { a, b ->
                a.rotation(b)
            }.sum()

        fun entireAreaExcept(boundary: Set<Point>): Set<Point> {
            if (location in boundary) return emptySet()

            val visited = mutableSetOf(location)
            val queue = neighbours.toMutableList()
            while (queue.isNotEmpty()) {
                val c = queue.removeAt(0)
                if (c.location in boundary) continue

                if (visited.add(c.location)) {
                    queue.addAll(c.neighbours)
                }
            }
            return visited.toSet()
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

    fun mainLoop() = Loop(mainLoopStart)

    inner class Loop(val start: Node, val points: Set<Point>, val rotation: Int) {
        fun containedArea(): Int {
            return start.traverse().flatMap { (n, d) ->
                allNodesLookingInside(n, d) + allNodesLookingAheadInside(n,d)
            }.toSet().size
        }

        private fun allNodesLookingInside(n: Node, d: CardinalDirection) =
            (n.nodeAt(inside(d))?.entireAreaExcept(points) ?: emptySet()).asSequence()

        private fun allNodesLookingAheadInside(n: Node, d: CardinalDirection) =
            (n.nodeAt(d)?.nodeAt(inside(d))?.entireAreaExcept(points) ?: emptySet()).asSequence()

        private fun inside(d: CardinalDirection) = if (rotation < 0) d.right else d.left

        constructor(start: Node) : this(
            start,
            start.traverse().map { it.first.location }.toSet(),
            start.overallRotation()
        )

        val size: Int = points.size
    }
}
