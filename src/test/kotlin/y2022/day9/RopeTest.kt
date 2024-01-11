package y2022.day9

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection.*
import util.Point
import y2022.day9.Movement.Companion.traceTail

class RopeTest {

    private val rope = Rope(head = Point(0, 0))

    @Test
    fun `rope movement - tail follows`() {
        rope.move(N) shouldBe
                Rope(
                    head = Point(0, -1),
                    tail = Point(0, 0)
                )

        rope.move(N).move(N) shouldBe
                Rope(head = Point(0, -2), tail = Point(0, -1))

        rope.move(N).move(E) shouldBe
                Rope(head = Point(1, -1), tail = Point(0, 0))

        rope.move(W).move(W) shouldBe
                Rope(head = Point(-2, 0), tail = Point(-1, 0))

        rope.move(S).move(S) shouldBe
                Rope(head = Point(0, 2), tail = Point(0, 1))

        rope.move(E).move(E) shouldBe
                Rope(head = Point(2, 0), tail = Point(1, 0))

        rope.move(N).move(E).move(N) shouldBe
                Rope(head = Point(1, -2), tail = Point(1, -1))

        rope.move(N).move(E).move(E) shouldBe
                Rope(head = Point(2, -1), tail = Point(1, -1))

        rope.move(E).move(N).move(E) shouldBe
                Rope(head = Point(2, -1), tail = Point(1, -1))
    }

    @Test
    fun `take multiple steps`() {
        Movement(E, 4).applyTo(rope).let(Movement(N, 4)::applyTo) shouldBe
                Rope(head = Point(4, -4), tail = Point(4, -3))
    }

    @Test
    fun `parse and apply movements`() {
        Movement.of("R 4").applyTo(rope) shouldBe Rope(head = Point(4, 0), tail = Point(3, 0))

        Movement.of("R 4").trace(rope).toList() shouldBe listOf(
            rope,
            Rope(head = Point(1, 0), tail = Point(0, 0)),
            Rope(head = Point(2, 0), tail = Point(1, 0)),
            Rope(head = Point(3, 0), tail = Point(2, 0)),
            Rope(head = Point(4, 0), tail = Point(3, 0))
        )
    }

    @Test
    fun `execute many movements`() {
        Movement.parseMany(
            """
                R 4
                U 4
                L 3
                D 1
                R 4
                D 1
                L 5
                R 2
            """.trimIndent()
        ).traceTail(rope) shouldBe 13
    }

    @Test
    fun `part 1`() {
        Movement.parseMany(AOC.getInput("/2022/day9.txt")).traceTail(rope) shouldBe 42
    }
}