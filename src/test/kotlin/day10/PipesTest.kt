package day10

import AOC
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
        Network.parse("L").size shouldBe (Point(1, 1))
        Network.parse(".-.").size shouldBe (Point(3, 1))
        Network.parse(complexSmallPipe).size shouldBe (Point(5, 5))

        val network = Network.parse(aocInput)
        network.size shouldBe (Point(140, 140))

        println(network.toString())
    }

    @Test
    fun `traverse pipe`() {
        val startNode = Network.parse(simplePipe).startNode
        startNode.location shouldBe (Point(1, 1))

        startNode.traverse().map { (n) -> n.location }.toList() shouldBe listOf(
            (Point(1, 1)),
            (Point(2, 1)),
            (Point(3, 1)),
            (Point(3, 2)),
            (Point(3, 3)),
            (Point(2, 3)),
            (Point(1, 3)),
            (Point(1, 2))
        )

        startNode.traverse().count() shouldBe 8
    }

    @Test
    fun `part 1`() {
        Network.parse(complexSmallPipe).startNode.traverse().count() / 2 shouldBe 8
        Network.parse(aocInput).startNode.traverse().count() / 2 shouldBe 7102
    }

    @Test
    fun `find ground square right to pipe`() {
        Network.parse(simplePipe).startNode.traverse().mapNotNull { (n, d) ->
            n.neighbours[d.right]
        }.first { it.isGround() }.location shouldBe (Point(2, 2))
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