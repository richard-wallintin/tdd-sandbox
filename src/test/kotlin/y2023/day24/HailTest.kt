package y2023.day24

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Matrix.Companion.column
import util.Matrix.Companion.matrix
import y2023.day24.Hailstone.Companion.allPairs
import y2023.day24.Hailstone.Companion.intersectingHailStones

class HailTest {

    private val referenceInput = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent()

    @Test
    fun `parse hailstone`() {
        Hailstone.of("19, 13, 30 @ -2,  1, -2") shouldBe
                Hailstone(
                    position = Vector3D(19, 13, 30),
                    velocity = Vector3D(-2, 1, -2)
                )
    }

    @Test
    fun movement() {
        Hailstone.of("20, 19, 15 @ 1, -5, -3").next().position shouldBe
                Vector3D(21, 14, 12)
    }

    @Test
    fun `parse from actual data`() {
        Hailstone.of("260252047346974, 360095837456982, 9086018216578 @ 66, -174, 512").position shouldBe
                Vector3D(260252047346974, 360095837456982, 9086018216578)
    }

    @Test
    fun `find intersection`() {
        val a = Hailstone.of("19, 13, 30 @ -2, 1, -2")
        val b = Hailstone.of("18, 19, 22 @ -1, -1, -2")

        a.planeCross(b, 7L..27L) shouldBe true
    }

    @Test
    fun `compute all pairs`() {
        listOf(1, 2, 3).allPairs().toList() shouldBe listOf(
            1 to 2,
            1 to 3,
            2 to 3
        )
    }

    @Test
    fun `reference input pairwise check`() {
        Hailstone.ofMany(referenceInput).intersectingHailStones(7L..27L) shouldBe 2
    }

    private val hailstones = Hailstone.ofMany(AOC.getInput("/2023/day24.txt"))

    @Test
    fun `part 1`() {
        hailstones.intersectingHailStones(
            200000000000000L..400000000000000L
        ) shouldBe 12015
    }

    @Test
    fun `solve part 2 step by step`() {
        val s = hailstones.take(6).toList()

        val solutionXY = matrix(
            mkRow(s[0], s[1]),
            mkRow(s[1], s[2]),
            mkRow(s[2], s[3]),
            mkRow(s[3], s[4])
        ).solve(
            column(
                mkResult(s[0], s[1]),
                mkResult(s[1], s[2]),
                mkResult(s[2], s[3]),
                mkResult(s[3], s[4])
            )
        ).round()

        solutionXY shouldBe column(472612107765508, 270148844447628, -5, -333)

        val xr = solutionXY[0, 0].toLong()
        val yr = solutionXY[1, 0].toLong()
        val vxr = solutionXY[3, 0].toLong()

        val t0 = (s[0].position.x - xr) / (vxr - s[0].velocity.x)
        t0 shouldBe 532230727866
        val zr0 = s[0].position.z + s[0].velocity.z * t0

        val t1 = (s[1].position.x - xr) / (vxr - s[1].velocity.x)
        t1 shouldBe 733302300048
        val zr1 = s[1].position.z + s[1].velocity.z * t1

        val vzr = (zr0 - zr1) / (t0 - t1)
        val zr = zr0 - (vzr * t0)

        zr shouldBe 273604689965980

        (xr + yr + zr) shouldBe 1016365642179116
    }

    private fun mkResult(s0: Hailstone, s1: Hailstone) =
        -(s0.position.x * s0.velocity.y) +
                (s0.position.y * s0.velocity.x) +
                (s1.position.x * s1.velocity.y) -
                (s1.position.y * s1.velocity.x)

    private fun mkRow(s0: Hailstone, s1: Hailstone) = listOf(
        s1.velocity.y - s0.velocity.y,
        -(s1.velocity.x - s0.velocity.x),
        s1.position.x - s0.position.x,
        -(s1.position.y - s0.position.y)
    )
}