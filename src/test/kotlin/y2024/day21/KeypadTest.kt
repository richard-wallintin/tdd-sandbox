package y2024.day21

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import util.CardinalDirection.*
import y2024.day21.DirectionalKey.Companion.compact
import y2024.day21.DirectionalKey.Companion.moves
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
