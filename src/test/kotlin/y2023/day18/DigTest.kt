package y2023.day18

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.Point
import y2023.day18.Hole.Companion.digTrench

class DigTest {

    private val referenceInstructions = Instruction.ofMany(
        """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
        """.trimIndent()
    )

    private val referenceTrench = referenceInstructions.digTrench()

    @Test
    fun `parse instruction`() {
        Instruction.of("R 6 (#70c710)") shouldBe Instruction(
            direction = CardinalDirection.E,
            meters = 6,
            color = "#70c710"
        )
    }

    @Test
    fun `hole chain`() {
        val secondHole = Hole().dig(
            Instruction(direction = CardinalDirection.E, meters = 1, color = "#70c710")
        )

        secondHole shouldBe Hole(location = Point(1, 0), from = Hole())
        secondHole.direction shouldBe CardinalDirection.E
    }

    @Test
    fun `reference trench size`() {
        val trench = referenceTrench

        trench.length shouldBe 38
        trench.rotation shouldBe -3
    }

    @Test
    fun `compute interior`() {
        referenceTrench.interior.size shouldBe (62-38)
        referenceTrench.size shouldBe 62
    }

    @Test
    fun `part 1`() {
        Instruction.ofMany(AOC.getInput("/2023/day18.txt")).digTrench().size shouldBe 50465
    }
}