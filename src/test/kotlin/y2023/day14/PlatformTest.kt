package y2023.day14

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection

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

    private val referencePlatformAfterOneCycle = """
        .....#....
        ....#...O#
        ...OO##...
        .OO#......
        .....OOO#.
        .O#...O#.#
        ....O#....
        ......OOOO
        #...O###..
        #..OO#....
    """.trimIndent()

    private val referencePlatformAfterThreeCycles = """
        .....#....
        ....#...O#
        .....##...
        ..O#......
        .....OOO#.
        .O#...O#.#
        ....O#...O
        .......OOO
        #...O###.O
        #.OOO#...O
    """.trimIndent()

    @Test
    fun `lane load calculation`() {
        Lane.of("...O...").load shouldBe 4
        Lane.of("OOOO....##").load shouldBe (7..10).sum()
    }

    @Test
    fun `platform to lanes`() {
        Platform.of(referencePlatformTiltedNorth).load(CardinalDirection.N)
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
        Platform.of(referencePlatform).tilt(CardinalDirection.N) shouldBe
                Platform.of(referencePlatformTiltedNorth)
    }


    private val inputPlatform = Platform.of(AOC.getInput("/2023/day14.txt"))

    @Test
    fun `part 1`() {
        inputPlatform
            .tilt(CardinalDirection.N)
            .load(CardinalDirection.N) shouldBe 109665
    }

    @Test
    fun `free turning`() {
        Platform.of("AB\nCD").turn(CardinalDirection.N) shouldBe
                Platform.of("BD\nAC", CardinalDirection.N)

        Platform.of("AB\nCD").turn(CardinalDirection.S) shouldBe
                Platform.of("CA\nDB", CardinalDirection.S)

        Platform.of("AB\nCD").turn(CardinalDirection.E) shouldBe
                Platform.of("DC\nBA", CardinalDirection.E)

        Platform.of("AB\nCD").turn(CardinalDirection.W) shouldBe
                Platform.of("AB\nCD", CardinalDirection.W)

        Platform.of("AB\nCD").turn(CardinalDirection.N).turn(CardinalDirection.W) shouldBe
                Platform.of("AB\nCD", CardinalDirection.W)

        Platform.of("AB\nCD").turn(CardinalDirection.S).turn(CardinalDirection.W) shouldBe
                Platform.of("AB\nCD", CardinalDirection.W)

        Platform.of("AB\nCD")
            .turn(CardinalDirection.N)
            .turn(CardinalDirection.W)
            .turn(CardinalDirection.S)
            .turn(CardinalDirection.E)
            .turn(CardinalDirection.W) shouldBe
                Platform.of("AB\nCD", CardinalDirection.W)
    }

    @Test
    fun tiltCycle() {
        Platform.of(referencePlatform).tiltCycle() shouldBe Platform.of(
            referencePlatformAfterOneCycle
        )

        Platform.of(referencePlatform).tiltCycle(3) shouldBe Platform.of(
            referencePlatformAfterThreeCycles
        )

        Platform.of(referencePlatform).tiltCycle(1_000_000_000).load(CardinalDirection.N) shouldBe 64
    }

    @Test
    fun `part 2`() {
        inputPlatform.tiltCycle(1_000_000_000).load(CardinalDirection.N) shouldBe 96061
    }
}

