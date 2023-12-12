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


    @Test
    fun `parse map`() {
        Network.parse("L").size shouldBe (Point(1, 1))
        Network.parse(".-.").size shouldBe (Point(3, 1))
        complexSmallPipeNetwork.size shouldBe (Point(5, 5))

        val network = aocPipe
        network.size shouldBe (Point(140, 140))

        println(network.toString())
    }

    @Test
    fun `traverse pipe`() {
        val startNode = simplePipeNetwork.mainLoopStart
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
        complexSmallPipeNetwork.mainLoopStart.traverse().count() / 2 shouldBe 8
        aocPipe.mainLoopStart.traverse().count() / 2 shouldBe 7102
    }

    @Test
    fun rotation() {
        Direction.N.rotation(Direction.E) shouldBe -1
        Direction.N.rotation(Direction.W) shouldBe 1
        Direction.N.rotation(Direction.N) shouldBe 0
        Direction.N.rotation(Direction.S) shouldBe 0
    }

    @Test
    fun `pipe overall rotation`() {
        simplePipeNetwork.mainLoopStart.overallRotation() shouldBe -3
        complexSmallPipeNetwork.mainLoopStart.overallRotation() shouldBe -3
        aocPipe.mainLoopStart.overallRotation() shouldBe -3
    }

    @Test
    fun `pipe loop as an object`() {
        simplePipeNetwork.mainLoop().containedArea() shouldBe 1
        complexSmallPipeNetwork.mainLoop().containedArea() shouldBe 1
        mediumComplexNetwork.mainLoop().containedArea() shouldBe 10
        specialConvexNetwork.mainLoop().containedArea() shouldBe 4
    }

    @Test
    fun part2() {
        aocPipe.mainLoop().containedArea() shouldBe 363
    }

    private val simplePipe = """
        .....
        .S-7.
        .|.|.
        .L-J.
        .....
    """.trimIndent()

    private val simplePipeNetwork = Network.parse(simplePipe)

    private val complexSmallPipe = """
        ..F7.
        .FJ|.
        SJ.L7
        |F--J
        LJ...
    """.trimIndent()

    private val complexSmallPipeNetwork = Network.parse(complexSmallPipe)

    private val mediumComplexNetwork = Network.parse(
        """
        FF7FSF7F7F7F7F7F---7
        L|LJ||||||||||||F--J
        FL-7LJLJ||||||LJL-77
        F--JF--7||LJLJ7F7FJ-
        L---JF-JLJ.||-FJLJJ7
        |F|F-JF---7F7-L7L|7|
        |FFJF7L7F-JF7|JL---7
        7-L-JL7||F7|L7F-7F7|
        L.L7LFJ|||||FJL7||LJ
        L7JLJL-JLJLJL--JLJ.L
    """.trimIndent()
    )

    private val specialConvexNetwork = Network.parse(
        """
        ...........
        .S-------7.
        .|F-----7|.
        .||.....||.
        .||.....||.
        .|L-7.F-J|.
        .|..|.|..|.
        .L--J.L--J.
        ...........
    """.trimIndent()
    )

    private val aocPipe = Network.parse(AOC.getInput("/day10.txt"))
}
