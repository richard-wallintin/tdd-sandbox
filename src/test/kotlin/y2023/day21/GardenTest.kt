package y2023.day21

import AOC
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
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

    @Test
    fun `read garden map`() {
        val garden = referenceGarden

        garden.size shouldBe Point(11, 11)
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
        referenceGarden.reach(6) shouldBe 16
    }

    @Test
    fun `part 1`() {
        Garden.of(AOC.getInput("/2023/day21.txt")).reach(64) shouldBe 3724
    }
}