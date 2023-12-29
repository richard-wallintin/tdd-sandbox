package y2023.day21

import AOC
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Parabola
import util.Point

class GardenTest {

    private val referenceInput = """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
    """.trimIndent()

    private val referenceGarden = Garden.of(referenceInput)
    private val inputGarden = Garden.of(AOC.getInput("/2023/day21.txt"))
    private val emptyGarden = Garden.of("S")

    @Test
    fun `read garden map`() {
        val garden = referenceGarden

        garden.baseSize shouldBe Point(11, 11)
        garden.start shouldBe Point(5, 5)
        garden.rocks shouldContain Point(5, 1)
        garden.rocks shouldContain Point(9, 9)
        garden.rocks.size shouldBe 40
    }

    @Test
    fun `find points to reach`() {
        referenceGarden.reach(1) shouldBe 2
        referenceGarden.reach(2) shouldBe 4
        referenceGarden.reach(3) shouldBe 6
        referenceGarden.reach(4) shouldBe 9
        referenceGarden.reach(5) shouldBe 13
        referenceGarden.reach(6) shouldBe 16
    }

    @Test
    fun `part 1`() {
        inputGarden.reach(64) shouldBe 3724
    }

    @Test
    fun `infinite garden`() {
        referenceGarden.reach(10) shouldBe 50
        referenceGarden.reach(50) shouldBe 1594
        referenceGarden.reach(100) shouldBe 6536
        referenceGarden.reach(500) shouldBe 167004
        // larger numbers take too long to compute (now)
    }

    @Test
    fun `express reachable tiles as growth rate`() {
        referenceGarden.shortestPath().take(1 + 20).toList() shouldBe
                listOf(
                    1,
                    2, 3, 4, 5, 7, 7, 9, 14, 19, 20,
                    22, 24, 26, 25, 26, 30, 30, 36, 47, 51
                )

        referenceGarden.totalReachable().take(7).toList() shouldBe listOf(
            1, 2, 4, 6, 9, 13, 16
        )
    }

    @Test
    fun `empty garden`() {
        val empty = emptyGarden
        empty.reach(1) shouldBe 4
        empty.reach(2) shouldBe 9
        empty.reach(3) shouldBe 16
        empty.reach(4) shouldBe 25
        empty.reach(5) shouldBe 36
    }

    @Test
    fun `quadratic function`() {
        Parabola.of(
            Point(-1, 1),
            Point(0, 0),
            Point(1, 1)
        ) shouldBe Parabola(a = 1, b = 0, c = 0)

        Parabola.of(
            Point(-1, 2),
            Point(0, 1),
            Point(1, 2)
        ) shouldBe Parabola(a = 1, b = 0, c = 1)
    }

    @Test
    fun `try some parabolas for the empty garden`() {
        val emptyGardenFormula = Parabola(1, 2, 1)
        Parabola.of(Point(1, 4), Point(2, 9), Point(3, 16)) shouldBe
                emptyGardenFormula

        emptyGardenFormula(4) shouldBe 25
        emptyGardenFormula(5) shouldBe 36
        emptyGardenFormula(6) shouldBe 49
    }

    @Test
    fun `part 2`() {
        val steps = 26_501_365
        steps shouldBe 202_300 * 131 + 65

        val parabola = inputGarden.findParabola(26_501_365)
        parabola(131 + 65) shouldBe inputGarden.reach(131 + 65)
        parabola(2 * 131 + 65) shouldBe inputGarden.reach(2 * 131 + 65)
        parabola(3 * 131 + 65) shouldBe inputGarden.reach(3 * 131 + 65)
        parabola(26_501_365) shouldBe 620348631910321L
    }
}