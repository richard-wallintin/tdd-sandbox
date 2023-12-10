package day10

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PipesTest {
    @Test
    fun `pipe segments`() {
        PipeSegment.of("|").connects shouldBe vertical
        PipeSegment.of("-").connects shouldBe horizontal
        PipeSegment.of("L").connects shouldBe cornerNE
        PipeSegment.of("J").connects shouldBe cornerNW
        PipeSegment.of("7").connects shouldBe cornerSW
        PipeSegment.of("F").connects shouldBe cornernSE
        PipeSegment.of("S").connects shouldBe start
        PipeSegment.of(".").connects shouldBe ground
    }

    private val aocInput = AOC.getInput("/day10.txt")

    @Test
    fun `parse map`() {
        Network.parse("L").size shouldBe (1 to 1)
        Network.parse(".-.").size shouldBe (3 to 1)
        Network.parse(complexSmallPipe).size shouldBe (5 to 5)

        val network = Network.parse(aocInput)
        network.size shouldBe (140 to 140)

        println(network.toString())
    }

    @Test
    fun `traverse pipe`() {
        val startNode = Network.parse(simplePipe).startNode
        startNode.location shouldBe (1 to 1)

        startNode.traverse().map { it.location }.toList() shouldBe listOf(
            (1 to 1),
            (2 to 1),
            (3 to 1),
            (3 to 2),
            (3 to 3),
            (2 to 3),
            (1 to 3),
            (1 to 2)
        )

        startNode.traverse().count() shouldBe 8
    }

    @Test
    fun `part 1`() {
        Network.parse(complexSmallPipe).startNode.traverse().count() / 2 shouldBe 8
        Network.parse(aocInput).startNode.traverse().count() / 2 shouldBe 7102
    }

    private val simplePipe = """
        .....
        .S-7.
        .|.|.
        .L-J.
        .....
    """.trimIndent()

    private val complexSmallPipe = """
        ..F7.
        .FJ|.
        SJ.L7
        |F--J
        LJ...
    """.trimIndent()
}