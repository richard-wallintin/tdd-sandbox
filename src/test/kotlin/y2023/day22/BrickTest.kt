package y2023.day22

import AOC
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import util.Point3D
import y2023.day22.Brick.Companion.fallPotential
import y2023.day22.Brick.Companion.safe
import y2023.day22.BrickStack.Companion.settle

class BrickTest {

    private val referenceInput = """
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
    """.trimIndent()

    private val inputBricks = Brick.ofMany(AOC.getInput("/2023/day22.txt"))

    @Test
    fun `read a brick`() {
        val brick = Brick.of("1,0,1~1,2,1")

        brick.start shouldBe Point3D(1, 0, 1)
        brick.end shouldBe Point3D(1, 2, 1)
    }

    @Test
    fun `brick volumes`() {
        Brick.of("2,2,2~2,2,2").volume shouldBe 1
        Brick.of("0,0,10~1,0,10").volume shouldBe 2
        Brick.of("0,0,1~0,0,10").volume shouldBe 10
    }

    @Test
    fun `brick bottom (resting level)`() {
        Brick.of("5,5,1~5,6,1").bottom shouldBe 1
        Brick.of("3,3,2~3,3,3").bottom shouldBeGreaterThan 1
    }

    @Test
    fun `determine horizontal overlap`() {
        val (a, b, c) = Brick.ofMany(referenceInput)

        a.bottom shouldBe 1
        a.top shouldBe 1

        b.bottom shouldBe 2
        b.top shouldBe 2

        b.horizontalOverlap(a) shouldBe true
        b.horizontalOverlap(c) shouldBe false
        a.horizontalOverlap(c) shouldBe true
    }

    @Test
    fun `settle bricks`() {
        val all = Brick.ofMany(referenceInput)
        val (a, b, c, d, e) = all
        val f = all[5]
        val g = all[6]

        listOf(a).settle() shouldBe listOf(a)
        listOf(a, b).settle() shouldBe listOf(a, b)
        listOf(a, b, c).settle() shouldBe listOf(
            a, b,
            c.elevate(-1)
        )

        listOf(a.elevate(+3)).settle() shouldBe listOf(a)

        listOf(a, b, c, d, e, f, g).settle() shouldBe listOf(
            a, b,
            c.elevate(-1),
            d.elevate(-1),
            e.elevate(-2),
            f.elevate(-2),
            g.elevate(-3),
        )
    }

    @Test
    fun `settle edge-touching case`() {
        Brick.of("0,0,1~0,0,1").horizontalOverlap(Brick.of("1,1,3~1,1,3")) shouldBe false

        listOf(
            Brick.of("0,0,1~0,0,1"),
            Brick.of("1,1,3~1,1,3")
        ).settle() shouldBe listOf(
            Brick.of("0,0,1~0,0,1"),
            Brick.of("1,1,1~1,1,1")
        )
    }

    @Test
    fun `dont settle on shorter brick`() {
        listOf(
            Brick.of("0,0,8~1,0,8"),   // 2.
            Brick.of("1,1,5~1,1,7"),   // 1.
            Brick.of("1,0,10~1,1,10"), // 3.
        ).settle() shouldBe listOf(
            Brick.of("1,1,1~1,1,3"),
            Brick.of("0,0,1~1,0,1"),
            Brick.of("1,0,4~1,1,4"), // on top of 1., not 2.
        )
    }


    @Test
    fun `find safe to disintegrate brick count`() {
        Brick.ofMany(referenceInput).safe() shouldBe 5
    }


    @Test
    fun `part 1`() {
        inputBricks.safe() shouldBeLessThan 1012
    }

    @Test
    fun `fall potential for reference`() {
        Brick.ofMany(referenceInput).fallPotential() shouldBe 7
    }

    @Test
    fun `part 2`() {
        inputBricks.fallPotential() shouldBe 74594
    }

    @Nested
    inner class WithReferenceStack {
        private val stack = BrickStack.of(Brick.ofMany(referenceInput))
        private val a = stack.settled[0]
        private val b = stack.settled[1]
        private val c = stack.settled[2]
        private val d = stack.settled[3]
        private val e = stack.settled[4]
        private val f = stack.settled[5]
        private val g = stack.settled[6]

        @Test
        fun `compute support graph`() {
            stack.support(a) shouldBe listOf(b, c)
            stack.supportedCount(a) shouldBe 0
            stack.supportedCount(b) shouldBe 1
            stack.supportedCount(c) shouldBe 1

            stack.support(b) shouldBe listOf(d, e)
            stack.support(c) shouldBe listOf(d, e)
            stack.support(d) shouldBe listOf(f)
            stack.support(e) shouldBe listOf(f)
            stack.support(f) shouldBe listOf(g)
            stack.support(g) shouldBe listOf()
        }

        @Test
        fun `compute chain reaction potential`() {
            stack.fall(a) shouldBe 6
            stack.fall(b) shouldBe 0
            stack.fall(f) shouldBe 1
            stack.fall(g) shouldBe 0
        }
    }
}


