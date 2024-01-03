package y2023.day24

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
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

    @Test
    fun `part 1`() {
        Hailstone.ofMany(AOC.getInput("/2023/day24.txt")).intersectingHailStones(
            200000000000000L..400000000000000L
        ) shouldBe 12015
    }
}