package y2023.day16

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Direction
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
        Tile('.').divert(Direction.N) shouldBe listOf(Direction.N)
        Tile('.').divert(Direction.W) shouldBe listOf(Direction.W)

        Tile('|').divert(Direction.W) shouldBe listOf(Direction.N, Direction.S)
        Tile('|').divert(Direction.E) shouldBe listOf(Direction.N, Direction.S)
        Tile('|').divert(Direction.N) shouldBe listOf(Direction.N)
        Tile('|').divert(Direction.S) shouldBe listOf(Direction.S)

        Tile('-').divert(Direction.W) shouldBe listOf(Direction.W)
        Tile('-').divert(Direction.E) shouldBe listOf(Direction.E)
        Tile('-').divert(Direction.N) shouldBe listOf(Direction.W, Direction.E)
        Tile('-').divert(Direction.S) shouldBe listOf(Direction.W, Direction.E)

        Tile('/').divert(Direction.W) shouldBe listOf(Direction.S)
        Tile('/').divert(Direction.E) shouldBe listOf(Direction.N)
        Tile('/').divert(Direction.N) shouldBe listOf(Direction.E)
        Tile('/').divert(Direction.S) shouldBe listOf(Direction.W)

        Tile('\\').divert(Direction.W) shouldBe listOf(Direction.N)
        Tile('\\').divert(Direction.E) shouldBe listOf(Direction.S)
        Tile('\\').divert(Direction.N) shouldBe listOf(Direction.W)
        Tile('\\').divert(Direction.S) shouldBe listOf(Direction.E)
    }

    @Test
    fun `beam diversion`() {
        Beam(location = Point(1, 0), direction = Direction.E).encounter(Tile('|')) shouldBe
                listOf(
                    Beam(location = Point(1, 0), direction = Direction.N),
                    Beam(location = Point(1, 0), direction = Direction.S)
                )
    }

    @Test
    fun `beam travels`() {
        val trace = reference.trace(Beam(location = Point(0, 0), direction = Direction.E))

        trace.take(4).toList() shouldBe listOf(
            Beam(location = Point(0, 0), direction = Direction.E),
            Beam(location = Point(1, 0), direction = Direction.E),
            Beam(location = Point(1, 1), direction = Direction.S),
            Beam(location = Point(1, 2), direction = Direction.S)
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
