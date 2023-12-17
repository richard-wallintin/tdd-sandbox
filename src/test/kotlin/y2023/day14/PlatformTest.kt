package y2023.day14

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Direction

class PlatformTest {

    private val referencePlatformTiltedNorth = """
        OOOO.#.O..
        OO..#....#
        OO..O##..O
        O..#.OO...
        ........#.
        ..#....#.#
        ..O..#.O.O
        ..O.......
        #....###..
        #....#....
    """.trimIndent()

    private val referencePlatform = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent()

    @Test
    fun `lane load calculation`() {
        Lane.of("...O...").load shouldBe 4
        Lane.of("OOOO....##").load shouldBe (7..10).sum()
    }

    @Test
    fun `platform to lanes`() {
        Platform.of(referencePlatformTiltedNorth).load(Direction.N)
    }

    @Test
    fun `lane tilting`() {
        Lane.of("O.").tilt() shouldBe Lane.of("O.")
        Lane.of(".O.").tilt() shouldBe Lane.of("O..")
        Lane.of("..O..O..").tilt() shouldBe Lane.of("OO......")

        Lane.of("#.O.").tilt() shouldBe Lane.of("#O..")
        Lane.of("##.O.").tilt() shouldBe Lane.of("##O..")
        Lane.of("..O.##.O.").tilt() shouldBe Lane.of("O...##O..")
        Lane.of("..O.##.O.#").tilt() shouldBe Lane.of("O...##O..#")
    }

    @Test
    fun `platform tilting`() {
        Platform.of(referencePlatform).tilt(Direction.N) shouldBe
                Platform.of(referencePlatformTiltedNorth)
    }


    @Test
    fun `part 1`() {
        Platform.of(AOC.getInput("/2023/day14.txt"))
            .tilt(Direction.N)
            .load(Direction.N) shouldBe 42
    }
}

