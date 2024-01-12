package y2022.day9

import util.CardinalDirection
import util.Point

abstract class AbstractRope {
    abstract val head: Point
    abstract val tail: Point
    abstract val points: List<Point>
    fun move(d: CardinalDirection) = moveHead(head.go(d))
    abstract fun moveHead(newHead: Point): AbstractRope
}

data class Rope(override val head: Point, override val tail: Point = head) : AbstractRope() {

    override val points: List<Point>
        get() = listOf(head, tail)

    override fun moveHead(newHead: Point): Rope {
        val newTail = tail + pull(newHead - tail)

        return copy(head = newHead, tail = newTail)
    }

    private fun pull(direction: Point) = when (direction) {
        Point(0, 2) -> Point(0, 1)
        Point(1, 2), Point(2, 2) -> Point(1, 1)
        Point(-1, 2) -> Point(-1, 1)

        Point(0, -2) -> Point(0, -1)
        Point(1, -2) -> Point(1, -1)
        Point(-1, -2), Point(-2, -2) -> Point(-1, -1)

        Point(2, 0) -> Point(1, 0)
        Point(2, 1) -> Point(1, 1)
        Point(2, -1), Point(2, -2) -> Point(1, -1)

        Point(-2, 0) -> Point(-1, 0)
        Point(-2, 1), Point(-2, 2) -> Point(-1, 1)
        Point(-2, -1) -> Point(-1, -1)
        else -> Point(0, 0)
    }
}

data class LongRope(val headRope: Rope, val tailRope: AbstractRope) : AbstractRope() {
    override val head: Point get() = headRope.head
    override val tail: Point get() = tailRope.tail

    override val points: List<Point>
        get() = listOf(head) + tailRope.points

    override fun moveHead(newHead: Point): AbstractRope {
        val newHeadRope = headRope.moveHead(newHead)
        val newTailRope = tailRope.moveHead(newHeadRope.tail)

        return copy(headRope = newHeadRope, tailRope = newTailRope)
    }

    companion object {
        fun of(head: Point, knots: Int): AbstractRope {
            require(knots > 2)
            return LongRope(
                headRope = Rope(head, head),
                tailRope = if (knots > 3) of(head, knots - 1) else Rope(head, head)
            )
        }
    }
}