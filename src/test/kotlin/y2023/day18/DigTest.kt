package y2023.day18

import AOC
import io.kotest.matchers.ints.shouldBeGreaterThan
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
            units = 6
        )
    }

    @Test
    fun `hole chain`() {
        val secondHole = Hole().dig(
            Instruction(direction = CardinalDirection.E, units = 1)
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
        referenceTrench.interior.size shouldBe (62 - 38)
        referenceTrench.size shouldBe 62
    }

    private val input = AOC.getInput("/2023/day18.txt")

    @Test
    fun `part 1`() {
        Instruction.ofMany(input).digTrench().size shouldBe 50465
    }

    @Test
    fun `parse instruction hex code`() {
        Instruction.of("R 6 (#70c710)", hex = true) shouldBe Instruction(
            direction = CardinalDirection.E,
            units = 461937
        )

        Instruction.of("L 2 (#5713f0)", hex = true) shouldBe Instruction(
            direction = CardinalDirection.E,
            units = 356671
        )
    }

    @Test
    fun `map instructions to a simplified tile grid`() {
        val grid = TileGrid.from(referenceInstructions)
        grid.size shouldBe Point(5, 5)

        val points = referenceInstructions.toList().runningFold(Point(0, 0)) { p, i ->
            p.go(i.direction, i.units)
        }

        val segments = points.zipWithNext()
        segments.size shouldBe 14

        segments.zip(referenceInstructions.toList()).size shouldBe 14

        grid.mappedInstructions.take(6).toList() shouldBe listOf(
            Instruction(CardinalDirection.E, 4),
            Instruction(CardinalDirection.S, 2),
            Instruction(CardinalDirection.W, 1),
            Instruction(CardinalDirection.S, 1),
            Instruction(CardinalDirection.E, 1),
            Instruction(CardinalDirection.S, 1),
        )

        grid.fullSizeOf(Point(0, 0)) shouldBe 2
        grid.fullSizeOf(Point(2, 0)) shouldBe 4
        grid.fullSizeOf(Point(2, 1)) shouldBe 6

        grid.trenchSize() shouldBe 62
    }

    @Test
    fun `use grid tile algorithm for part 1`() {
        val grid = TileGrid.from(Instruction.ofMany(input))

        grid.trenchSize() shouldBe 50465
    }

    @Test
    fun `part 2 WIP`() {
        val instructions = Instruction.ofMany(input, true)
        val grid = TileGrid.from(instructions)

        grid.size shouldBe Point(282, 286)

        val trenchSize = grid.trenchSize()
        trenchSize shouldBeGreaterThan 494599194
        println(trenchSize)
    }
}