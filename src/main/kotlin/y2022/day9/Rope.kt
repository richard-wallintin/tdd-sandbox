package y2022.day9

import util.CardinalDirection
import util.Point

data class Rope(val head: Point, val tail: Point = head) {
    fun move(d: CardinalDirection): Rope {
        val newHead = head.go(d)

        val diff = newHead - tail
        val newTail = when (diff.abs()) {
            Point(0, 2) -> head
            Point(1, 2) -> head
            Point(2, 0) -> head
            Point(2, 1) -> head
            else -> tail
        }

        return copy(head = newHead, tail = newTail)
    }
}
