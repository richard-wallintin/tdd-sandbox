package y2023.day16

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.Point
import y2023.day16.Beam.Companion.uniqueLocations

class ContraptionTest {


    private val reference = Contraption.parse(
        """
                .|...\....
                |.-.\.....
                .....|-...
                ........|.
                ..........
                .........\
                ..../.\\..
                .-.-/..|..
                .|....-|.\
                ..//.|....
            """.trimIndent()
    )

    @Test
    fun `parse contraption`() {
        reference.size shouldBe Point(10, 10)
    }

    @Test
    fun encounters() {
        Tile('.').divert(CardinalDirection.N) shouldBe listOf(CardinalDirection.N)
        Tile('.').divert(CardinalDirection.W) shouldBe listOf(CardinalDirection.W)

        Tile('|').divert(CardinalDirection.W) shouldBe listOf(CardinalDirection.N, CardinalDirection.S)
        Tile('|').divert(CardinalDirection.E) shouldBe listOf(CardinalDirection.N, CardinalDirection.S)
        Tile('|').divert(CardinalDirection.N) shouldBe listOf(CardinalDirection.N)
        Tile('|').divert(CardinalDirection.S) shouldBe listOf(CardinalDirection.S)

        Tile('-').divert(CardinalDirection.W) shouldBe listOf(CardinalDirection.W)
        Tile('-').divert(CardinalDirection.E) shouldBe listOf(CardinalDirection.E)
        Tile('-').divert(CardinalDirection.N) shouldBe listOf(CardinalDirection.W, CardinalDirection.E)
        Tile('-').divert(CardinalDirection.S) shouldBe listOf(CardinalDirection.W, CardinalDirection.E)

        Tile('/').divert(CardinalDirection.W) shouldBe listOf(CardinalDirection.S)
        Tile('/').divert(CardinalDirection.E) shouldBe listOf(CardinalDirection.N)
        Tile('/').divert(CardinalDirection.N) shouldBe listOf(CardinalDirection.E)
        Tile('/').divert(CardinalDirection.S) shouldBe listOf(CardinalDirection.W)

        Tile('\\').divert(CardinalDirection.W) shouldBe listOf(CardinalDirection.N)
        Tile('\\').divert(CardinalDirection.E) shouldBe listOf(CardinalDirection.S)
        Tile('\\').divert(CardinalDirection.N) shouldBe listOf(CardinalDirection.W)
        Tile('\\').divert(CardinalDirection.S) shouldBe listOf(CardinalDirection.E)
    }

    @Test
    fun `beam diversion`() {
        Beam(location = Point(1, 0), direction = CardinalDirection.E).encounter(Tile('|')) shouldBe
                listOf(
                    Beam(location = Point(1, 0), direction = CardinalDirection.N),
                    Beam(location = Point(1, 0), direction = CardinalDirection.S)
                )
    }

    @Test
    fun `beam travels`() {
        val trace = reference.trace(Beam(location = Point(0, 0), direction = CardinalDirection.E))

        trace.take(4).toList() shouldBe listOf(
            Beam(location = Point(0, 0), direction = CardinalDirection.E),
            Beam(location = Point(1, 0), direction = CardinalDirection.E),
            Beam(location = Point(1, 1), direction = CardinalDirection.S),
            Beam(location = Point(1, 2), direction = CardinalDirection.S)
        )

        trace.count() shouldBe 51
        trace.uniqueLocations().size shouldBe 46

        reference.illumination() shouldBe 46
    }

    private val input = Contraption.parse(AOC.getInput("/2023/day16.txt"))

    @Test
    fun `part 1`() {
        input.illumination() shouldBe 7996
    }

    @Test
    fun `max energy`() {
        reference.maxIllumination() shouldBe 51
    }

    @Test
    fun `part 2`() {
        input.maxIllumination() shouldBe 8239
    }
}
