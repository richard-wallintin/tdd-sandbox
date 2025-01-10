package y2024.day21

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.CardinalDirection.*
import util.CardinalDirection.Companion.inverse
import util.Memo
import util.chunkedBy
import util.combine
import y2024.day21.DirectionalKey.Companion.compact
import y2024.day21.DirectionalKey.Companion.moves
import y2024.day21.DirectionalKey.Companion.presses
import y2024.day21.Key.Companion.inverse
import y2024.day21.NumericKey.Companion.moves
import y2024.day21.NumericKey.Companion.moves3

class KeypadTest {

    @Test
    fun `numeric keypad moves`() {
        NumericKey.KEY_A.moveTo(NumericKey.KEY_0) shouldBe setOf(listOf(W))

        NumericKey.KEY_A.moveTo(NumericKey.KEY_3) shouldBe setOf(listOf(N))
        NumericKey.KEY_A.moveTo(NumericKey.KEY_2) shouldBe setOf(listOf(N, W), listOf(W, N))
        NumericKey.KEY_A.moveTo(NumericKey.KEY_1) shouldBe setOf(listOf(N, W, W), listOf(W, N, W))

        NumericKey.KEY_A.moveTo(NumericKey.KEY_6) shouldBe setOf(listOf(N, N))
        NumericKey.KEY_A.moveTo(NumericKey.KEY_5) shouldBe setOf(
            listOf(N, N, W),
            listOf(W, N, N),
            listOf(N, W, N)
        )
        NumericKey.KEY_A.moveTo(NumericKey.KEY_4) shouldBe setOf(
            listOf(N, N, W, W),
            listOf(N, W, N, W),
            listOf(W, N, W, N),
            listOf(W, N, N, W),
            listOf(N, W, W, N)
        )

        NumericKey.KEY_A.moveTo(NumericKey.KEY_9) shouldBe setOf(listOf(N, N, N))
        NumericKey.KEY_A.moveTo(NumericKey.KEY_8) shouldBe setOf(
            listOf(N, N, N, W),
            listOf(N, N, W, N),
            listOf(N, W, N, N),
            listOf(W, N, N, N)
        )
        val aTo7 = NumericKey.KEY_A.moveTo(NumericKey.KEY_7)
        aTo7 shouldHaveSize 9
        aTo7 shouldContain listOf(N, W, N, W, N)
        aTo7 shouldNotContain listOf(W, W, N, N, N)

        NumericKey.KEY_0.moveTo(NumericKey.KEY_A) shouldBe setOf(listOf(E))

        NumericKey.KEY_0.moveTo(NumericKey.KEY_3) shouldBe setOf(listOf(N, E), listOf(E, N))
        NumericKey.KEY_0.moveTo(NumericKey.KEY_2) shouldBe setOf(listOf(N))
        NumericKey.KEY_0.moveTo(NumericKey.KEY_1) shouldBe setOf(listOf(N, W))

        NumericKey.KEY_0.moveTo(NumericKey.KEY_6) shouldBe setOf(
            listOf(N, N, E),
            listOf(N, E, N),
            listOf(E, N, N)
        )
        NumericKey.KEY_0.moveTo(NumericKey.KEY_5) shouldBe setOf(listOf(N, N))
        NumericKey.KEY_0.moveTo(NumericKey.KEY_4) shouldBe setOf(listOf(N, N, W), listOf(N, W, N))

        NumericKey.KEY_0.moveTo(NumericKey.KEY_9) shouldHaveSize 4
        NumericKey.KEY_0.moveTo(NumericKey.KEY_8) shouldBe setOf(listOf(N, N, N))
        NumericKey.KEY_0.moveTo(NumericKey.KEY_7) shouldHaveSize 3

        NumericKey.KEY_3.moveTo(NumericKey.KEY_2) shouldBe setOf(listOf(W))
        NumericKey.KEY_3.moveTo(NumericKey.KEY_1) shouldBe setOf(listOf(W, W))

        NumericKey.KEY_2.moveTo(NumericKey.KEY_1) shouldBe setOf(listOf(W))

        NumericKey.KEY_7.moveTo(NumericKey.KEY_1) shouldBe setOf(listOf(S, S))
        NumericKey.KEY_7.moveTo(NumericKey.KEY_2) shouldHaveSize 3
        NumericKey.KEY_8.moveTo(NumericKey.KEY_1) shouldHaveSize 3
    }

    @Test
    fun `really all keypad moves are defined`() {
        NumericKey.entries.forEach { from ->
            NumericKey.entries.forEach { to ->
                withClue("$from <=> $to") {
                    from.moveTo(to) shouldBe to.moveTo(from).inverse()
                }
            }
        }
    }


    @Test
    fun `directional keypad moves`() {
        DirectionalKey.UP.moveTo(DirectionalKey.A) shouldBe setOf(listOf(E))
        DirectionalKey.UP.moveTo(DirectionalKey.UP) shouldBe setOf(emptyList())
        DirectionalKey.UP.moveTo(DirectionalKey.DOWN) shouldBe setOf(listOf(S))
        DirectionalKey.UP.moveTo(DirectionalKey.LEFT) shouldBe setOf(listOf(S, W))
        DirectionalKey.UP.moveTo(DirectionalKey.RIGHT) shouldBe setOf(listOf(S, E), listOf(E, S))

        DirectionalKey.LEFT.moveTo(DirectionalKey.A) shouldBe setOf(
            listOf(E, N, E),
            listOf(E, E, N)
        )
        DirectionalKey.RIGHT.moveTo(DirectionalKey.A) shouldBe setOf(listOf(N))
        DirectionalKey.DOWN.moveTo(DirectionalKey.A) shouldBe setOf(listOf(N, E), listOf(E, N))
        DirectionalKey.RIGHT.moveTo(DirectionalKey.LEFT) shouldBe setOf(listOf(W, W))
    }

    @Test
    fun `all keypad moves`() {
        DirectionalKey.entries.forEach { from ->
            DirectionalKey.entries.forEach { to ->
                withClue("$from <=> $to") {
                    from.moveTo(to) shouldBe to.moveTo(from).inverse()
                }
            }
        }
    }

    @Test
    fun `direct instructions for keypad robot`() {

        val keys = NumericKey.of("029A")

        keys shouldBe listOf(
            NumericKey.KEY_0,
            NumericKey.KEY_2,
            NumericKey.KEY_9,
            NumericKey.KEY_A
        )

        val directMoves = keys.moves()

        directMoves.compact() shouldContain "<A^A^^>AvvvA"

        val indirectMoves = directMoves.moves()

        indirectMoves.compact() shouldContain "v<<A>>^A<A>AvA<^AA>A<vAAA>^A"

        indirectMoves.moves()
            .compact() shouldContain "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"
    }

    @Test
    fun `compute l3 moves for examples`() {
        NumericKey.of("980A").moves3()
            .compact() shouldContain "<v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A"
        NumericKey.of("179A").moves3()
            .compact() shouldContain "<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A"
        NumericKey.of("456A").moves3()
            .compact() shouldContain "<v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A"
        NumericKey.of("379A").moves3()
            .compact() shouldContain "<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A"
    }

    private val sampleCodes = """
        029A
        980A
        179A
        456A
        379A
    """.trimIndent().lineSequence()

    @Test
    fun `compute complexity`() {
        NumericKey.complexity("029A") shouldBe 68 * 29
        NumericKey.complexity("980A") shouldBe 60 * 980
        NumericKey.complexity("179A") shouldBe 68 * 179
        NumericKey.complexity("456A") shouldBe 64 * 456
        NumericKey.complexity("379A") shouldBe 64 * 379


        sampleCodes.sumOf { NumericKey.complexity(it) } shouldBe 126384
    }

    private val codes = """
                382A
                463A
                935A
                279A
                480A
            """.trimIndent().lineSequence()

    @Test
    fun part1() {
        codes.sumOf { NumericKey.complexity(it) } shouldBe 179444
    }

    @Test
    fun `compute key presses`() {
        NumericKey.presses("029A", order = 2) shouldBe 68
        NumericKey.presses("980A", order = 2) shouldBe 60
        NumericKey.presses("179A", order = 2) shouldBe 68
        NumericKey.presses("456A", order = 2) shouldBe 64
        NumericKey.presses("379A", order = 2) shouldBe 64
    }

    @Test
    fun part2() {
        codes.sumOf { NumericKey.highComplexity(it) } shouldBe 223285811665866L
    }
}

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
                A -> setOf(listOf(E))
                DOWN -> setOf(listOf(S))
                RIGHT -> via(DOWN, o) + via(A, o)
                else -> via(DOWN, o)
            }

            A -> when (o) {
                RIGHT -> setOf(listOf(S))
                UP -> invert(o)
                else -> via(UP, o) + via(RIGHT, o)
            }

            DOWN -> when (o) {
                LEFT -> setOf(listOf(W))
                RIGHT -> setOf(listOf(E))
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
            N -> UP
            S -> DOWN
            W -> LEFT
            E -> RIGHT
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
                KEY_0 -> setOf(listOf(W))
                KEY_3 -> setOf(listOf(N))
                KEY_6, KEY_9 -> via(KEY_3, o)
                else -> via(KEY_3, o) + via(KEY_0, o)
            }

            KEY_0 -> when (o) {
                KEY_2 -> setOf(listOf(N))
                KEY_A -> invert(o)
                KEY_9, KEY_6, KEY_3 -> via(KEY_2, o) + via(KEY_A, o)
                else -> via(KEY_2, o)
            }

            KEY_3 -> when (o) {
                KEY_2 -> setOf(listOf(W))
                KEY_6 -> setOf(listOf(N))
                KEY_1 -> via(KEY_2, o)
                KEY_A, KEY_0 -> invert(o)
                KEY_9 -> via(KEY_6, o)
                else -> via(KEY_6, o) + via(KEY_2, o)
            }

            KEY_2 -> when (o) {
                KEY_1 -> setOf(listOf(W))
                KEY_5 -> setOf(listOf(N))
                KEY_3, KEY_0, KEY_A -> invert(o)
                KEY_4, KEY_7 -> via(KEY_1, o) + via(KEY_5, o)
                KEY_8 -> via(KEY_5, o)
                else -> via(KEY_5, o) + via(KEY_3, o)
            }

            KEY_1 -> when (o) {
                KEY_4 -> setOf(listOf(N))
                KEY_7 -> via(KEY_4, o)
                KEY_5, KEY_6, KEY_8, KEY_9 -> via(KEY_4, o) + via(KEY_2, o)
                else -> invert(o)
            }

            KEY_6 -> when (o) {
                KEY_5 -> setOf(listOf(W))
                KEY_4 -> via(KEY_5, o)
                KEY_9 -> setOf(listOf(N))
                KEY_8, KEY_7 -> via(KEY_9, o) + via(KEY_5, o)
                else -> invert(o)
            }

            KEY_5 -> when (o) {
                KEY_4 -> setOf(listOf(W))
                KEY_8 -> setOf(listOf(N))
                KEY_7 -> via(KEY_8, o) + via(KEY_4, o)
                KEY_9 -> via(KEY_8, o) + via(KEY_6, o)
                else -> invert(o)
            }

            KEY_4 -> when (o) {
                KEY_7 -> setOf(listOf(N))
                KEY_8, KEY_9 -> via(KEY_5, o) + via(KEY_7, o)
                else -> invert(o)
            }

            KEY_9 -> when (o) {
                KEY_8 -> setOf(listOf(W))
                KEY_7 -> via(KEY_8, o)
                else -> invert(o)
            }

            KEY_8 -> when (o) {
                KEY_7 -> setOf(listOf(W))
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
