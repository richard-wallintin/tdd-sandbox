package y2022.day9

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection.*
import util.Point
import y2022.day9.Movement.Companion.trace
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

    private val sampleMovements = Movement.parseMany(
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
    )

    @Test
    fun `execute many movements`() {
        sampleMovements.traceTail(rope) shouldBe 13
    }

    private val movements = Movement.parseMany(AOC.getInput("/2022/day9.txt"))

    @Test
    fun `part 1`() {
        movements.traceTail(rope) shouldBe 6339
    }

    private val longRope10 = LongRope.of(head = Point(0, 0), knots = 10)

    @Test
    fun `long rope`() {
        longRope10.move(E).tail shouldBe Point(0, 0)

        Movement(E, 11).applyTo(LongRope.of(head = Point(0, 0), knots = 3)).tail shouldBe Point(
            9,
            0
        )

        Movement(E, 10).applyTo(longRope10).tail shouldBe Point(1, 0)
    }

    private val sampleLongMovements = Movement.parseMany(
        """
                R 5
                U 8
                L 8
                D 3
                R 17
                D 10
                L 25
                U 20
            """.trimIndent()
    )

    @Test
    fun `trace reference with long rope`() {
        sampleMovements.traceTail(longRope10) shouldBe 1

        sampleMovements.take(2).trace(longRope10).last().points shouldBe listOf(
            Point(4, -4),
            Point(4, -3),
            Point(4, -2),
            Point(3, -2),
            Point(2, -2),
            Point(1, -1),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0)
        )

        sampleMovements.trace(longRope10).last().points shouldBe listOf(
            Point(2, -2),
            Point(1, -2),
            Point(2, -2),
            Point(3, -2),
            Point(2, -2),
            Point(1, -1),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0)
        )

        val after2Steps = sampleLongMovements.take(2).trace(longRope10).last()
        after2Steps.points shouldBe listOf(
            Point(5, -8),
            Point(5, -7),
            Point(5, -6),
            Point(5, -5),
            Point(5, -4),
            Point(4, -4),
            Point(3, -3),
            Point(2, -2),
            Point(1, -1),
            Point(0, 0)
        )
        after2Steps.tail shouldBe Point(0, 0)

        sampleLongMovements.traceTail(longRope10) shouldBe 36
    }

    @Test
    fun `part 2`() {
        movements.traceTail(longRope10) shouldBe 2541
    }
}


