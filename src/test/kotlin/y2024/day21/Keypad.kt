package y2024.day21

import util.CardinalDirection
import util.CardinalDirection.Companion.inverse
import util.Memo
import util.chunkedBy
import util.combine
import y2024.day21.DirectionalKey.Companion.moves
import y2024.day21.DirectionalKey.Companion.presses

typealias PossibleMovements = Set<List<CardinalDirection>>

interface Key<T : Key<T>> {
    fun moveTo(o: T): PossibleMovements

    fun T.invert(o: T): PossibleMovements = o.moveTo(this).inverse()
    fun via(via: T, to: T): PossibleMovements = (moveTo(via) combine via.moveTo(to)).toSet()

    companion object {
        fun PossibleMovements.inverse() = map { it.inverse() }.toSet()
    }
}
typealias PossibleKeys<T> = Set<List<T>>

enum class DirectionalKey : Key<DirectionalKey> {
    DOWN,
    LEFT,
    RIGHT,
    UP,
    A;

    override fun moveTo(o: DirectionalKey) =
        if (this == o) setOf(emptyList()) else when (this) {
            UP -> when (o) {
                A -> setOf(listOf(CardinalDirection.E))
                DOWN -> setOf(listOf(CardinalDirection.S))
                RIGHT -> via(DOWN, o) + via(A, o)
                else -> via(DOWN, o)
            }

            A -> when (o) {
                RIGHT -> setOf(listOf(CardinalDirection.S))
                UP -> invert(o)
                else -> via(UP, o) + via(RIGHT, o)
            }

            DOWN -> when (o) {
                LEFT -> setOf(listOf(CardinalDirection.W))
                RIGHT -> setOf(listOf(CardinalDirection.E))
                UP -> invert(o)
                else -> via(UP, o) + via(RIGHT, o)
            }

            else -> when (o) {
                LEFT, RIGHT -> via(DOWN, o)
                else -> invert(o)
            }
        }

    companion object {
        fun key(d: CardinalDirection) = when (d) {
            CardinalDirection.N -> UP
            CardinalDirection.S -> DOWN
            CardinalDirection.W -> LEFT
            CardinalDirection.E -> RIGHT
        }

        fun PossibleKeys<DirectionalKey>.moves() = moves(A)

        fun <T : Key<T>> List<T>.moves(start: T): PossibleKeys<DirectionalKey> =
            (listOf(start) + this).zipWithNext { a, b ->
                a.moveTo(b).keys()
            }.reduce { a, b -> a combine b }.toSet()

        private fun PossibleMovements.keys() = map { it.keys() }
        private fun List<CardinalDirection>.keys(): List<DirectionalKey> = map(::key) + listOf(A)

        private fun <T> List<List<T>>.filterBestSize(): List<List<T>> {
            val bestSize = minOf { it.size }
            return filter { it.size == bestSize }
        }

        fun <T : Key<T>> PossibleKeys<T>.moves(start: T) = flatMap { keys -> keys.moves(start) }
            .filterBestSize() // crucial to avoid combinatorial explosion
            .toSet()

        fun PossibleKeys<DirectionalKey>.compact() = joinToString("\n") { it.compact() }
        fun List<DirectionalKey>.compact() = joinToString("") {
            when (it) {
                UP -> "^"
                DOWN -> "v"
                LEFT -> "<"
                RIGHT -> ">"
                A -> "A"
            }
        }

        data class MemoKey(val stroke: List<DirectionalKey>, val order: Int)

        fun PossibleKeys<DirectionalKey>.presses(
            order: Int,
            memo: Memo<MemoKey, Long> = Memo(),
        ): Long = minOf { it.presses(order, memo) }

        private fun List<DirectionalKey>.presses(
            order: Int,
            memo: Memo<MemoKey, Long>,
        ): Long {
            return if (order == 0) size.toLong()
            else asSequence().chunkedBy(keepDelimiter = true) { it == A }
                .sumOf { stroke -> strokePresses(stroke, order, memo) }
        }

        private fun strokePresses(
            stroke: List<DirectionalKey>,
            order: Int,
            memo: Memo<MemoKey, Long>,
        ) = memo.recall(MemoKey(stroke, order)) {
            stroke.moves(A).presses(order - 1, memo)
        }
    }
}

enum class NumericKey : Key<NumericKey> {
    KEY_A,
    KEY_0,

    KEY_3,
    KEY_2,
    KEY_1,

    KEY_6,
    KEY_5,
    KEY_4,

    KEY_9,
    KEY_8,
    KEY_7;

    override fun moveTo(o: NumericKey): PossibleMovements {
        return if (this == o) setOf(emptyList()) else when (this) {
            KEY_A -> when (o) {
                KEY_0 -> setOf(listOf(CardinalDirection.W))
                KEY_3 -> setOf(listOf(CardinalDirection.N))
                KEY_6, KEY_9 -> via(KEY_3, o)
                else -> via(KEY_3, o) + via(KEY_0, o)
            }

            KEY_0 -> when (o) {
                KEY_2 -> setOf(listOf(CardinalDirection.N))
                KEY_A -> invert(o)
                KEY_9, KEY_6, KEY_3 -> via(KEY_2, o) + via(KEY_A, o)
                else -> via(KEY_2, o)
            }

            KEY_3 -> when (o) {
                KEY_2 -> setOf(listOf(CardinalDirection.W))
                KEY_6 -> setOf(listOf(CardinalDirection.N))
                KEY_1 -> via(KEY_2, o)
                KEY_A, KEY_0 -> invert(o)
                KEY_9 -> via(KEY_6, o)
                else -> via(KEY_6, o) + via(KEY_2, o)
            }

            KEY_2 -> when (o) {
                KEY_1 -> setOf(listOf(CardinalDirection.W))
                KEY_5 -> setOf(listOf(CardinalDirection.N))
                KEY_3, KEY_0, KEY_A -> invert(o)
                KEY_4, KEY_7 -> via(KEY_1, o) + via(KEY_5, o)
                KEY_8 -> via(KEY_5, o)
                else -> via(KEY_5, o) + via(KEY_3, o)
            }

            KEY_1 -> when (o) {
                KEY_4 -> setOf(listOf(CardinalDirection.N))
                KEY_7 -> via(KEY_4, o)
                KEY_5, KEY_6, KEY_8, KEY_9 -> via(KEY_4, o) + via(KEY_2, o)
                else -> invert(o)
            }

            KEY_6 -> when (o) {
                KEY_5 -> setOf(listOf(CardinalDirection.W))
                KEY_4 -> via(KEY_5, o)
                KEY_9 -> setOf(listOf(CardinalDirection.N))
                KEY_8, KEY_7 -> via(KEY_9, o) + via(KEY_5, o)
                else -> invert(o)
            }

            KEY_5 -> when (o) {
                KEY_4 -> setOf(listOf(CardinalDirection.W))
                KEY_8 -> setOf(listOf(CardinalDirection.N))
                KEY_7 -> via(KEY_8, o) + via(KEY_4, o)
                KEY_9 -> via(KEY_8, o) + via(KEY_6, o)
                else -> invert(o)
            }

            KEY_4 -> when (o) {
                KEY_7 -> setOf(listOf(CardinalDirection.N))
                KEY_8, KEY_9 -> via(KEY_5, o) + via(KEY_7, o)
                else -> invert(o)
            }

            KEY_9 -> when (o) {
                KEY_8 -> setOf(listOf(CardinalDirection.W))
                KEY_7 -> via(KEY_8, o)
                else -> invert(o)
            }

            KEY_8 -> when (o) {
                KEY_7 -> setOf(listOf(CardinalDirection.W))
                else -> invert(o)
            }

            KEY_7 -> invert(o)
        }
    }

    companion object {
        fun of(text: String) = text.map { of(it) }
        fun of(c: Char) = when (c) {
            '9' -> KEY_9
            '8' -> KEY_8
            '7' -> KEY_7
            '6' -> KEY_6
            '5' -> KEY_5
            '4' -> KEY_4
            '3' -> KEY_3
            '2' -> KEY_2
            '1' -> KEY_1
            '0' -> KEY_0
            'A' -> KEY_A
            else -> throw IllegalArgumentException("no key for $c")
        }

        fun List<NumericKey>.moves() = moves(KEY_A)
        fun List<NumericKey>.moves3() = moves().moves().moves()

        fun complexity(code: String) = presses(code, order = 2) * numericValue(code)
        fun highComplexity(code: String) = presses(code, order = 25) * numericValue(code)

        private fun numericValue(code: String) = code.filter { it.isDigit() }.toInt()
        fun presses(code: String, order: Int): Long {
            val base = of(code).moves()

            return base.presses(order)
        }
    }
}