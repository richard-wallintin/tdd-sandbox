package y2023.day25

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import util.Edge
import util.WeightedGraph
import util.pick

class WireTest {

    private val referenceInput = """
        jqt: rhn xhk nvd
        rsh: frs pzl lsr
        xhk: hfx
        cmg: qnr nvd lhk bvb
        rhn: xhk bvb hfx
        bvb: xhk hfx
        pzl: lsr hfx nvd
        qnr: nvd
        ntq: jqt hfx bvb xhk
        nvd: lhk
        lsr: lhk
        rzs: qnr cmg lsr rsh
        frs: qnr lhk lsr
    """.trimIndent()

    private val referenceGraph = parseGraph(referenceInput)

    @Test
    fun `parse graph`() {
        configurations(referenceInput).take(1).toList() shouldBe listOf(
            "jqt" to listOf("rhn", "xhk", "nvd")
        )

        referenceGraph.edges.size shouldBe 33
    }

    @Test
    fun `compute number of groups`() {
        referenceGraph.groups.size shouldBe 1
    }

    @Test
    fun `separated graph`() {
        WeightedGraph(
            setOf(
                Edge("a", "b", 1),
                Edge("c", "d", 1)
            )
        ).groups.size shouldBe 2
    }

    @Test
    fun `remove example edges`() {
        val split = referenceGraph.removeEdges(
            setOf(
                Edge("hfx", "pzl"),
                Edge("bvb", "cmg"),
                Edge("nvd", "jqt")
            )
        )
        split.groups.size shouldBe 2
        split.splitWeight shouldBe 54
    }

    @Test
    fun `find split`() {
        referenceGraph.findSplit().first().groups.size shouldBe 2
    }

    @Test
    fun `pick n from list`() {
        listOf(1, 2, 3).pick(3).toList() shouldBe listOf(
            listOf(1, 2, 3)
        )

        listOf(1, 2, 3).pick(1).toList() shouldBe listOf(
            listOf(1), listOf(2), listOf(3)
        )

        listOf(1, 2, 3).pick(2).toList() shouldBe listOf(
            listOf(1, 2), listOf(1, 3), listOf(2, 3)
        )
    }

    @Test @Disabled("infinite..")
    fun `part 1`() {
        parseGraph(AOC.getInput("/2023/day25.txt")).findSplit().first().splitWeight shouldBe 42
    }

    private fun parseGraph(text: String) = WeightedGraph(
        configurations(text).toList().flatMap { (from, others) ->
            others.map { Edge(from, it, 1) }
        }.toSet()
    )

    private fun configurations(text: String) = text.lineSequence().map {
        val cfg = it.split(Regex("\\s+"))
        val module = cfg.first().dropLast(1)
        val connected = cfg.drop(1)
        module to connected
    }
}