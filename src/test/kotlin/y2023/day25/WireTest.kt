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

    private val referenceSplitEdges = setOf(
        Edge("hfx", "pzl"),
        Edge("bvb", "cmg"),
        Edge("nvd", "jqt")
    )

    @Test
    fun `remove example edges`() {
        val split = referenceGraph.removeEdges(referenceSplitEdges)
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

    private val graph = parseGraph(AOC.getInput("/2023/day25.txt"))

    @Test
    @Disabled("too slow, need better algorithm")
    fun `part 1`() {
        graph.findSplit().first().splitWeight shouldBe 42
    }

    @Test
    fun `get nodes from graph`() {
        referenceGraph.nodes.size shouldBe 15
        graph.nodes.size shouldBe 1423
    }

    @Test
    fun `compute edge flow`() {
        val g = parseGraph(
            """
            a: b
            b: c
            c: d e
        """.trimIndent()
        )

        g.edgeFlow("a") shouldBe mapOf(
            Edge("a", "b") to 4,
            Edge("b", "c") to 3,
            Edge("c", "d") to 1,
            Edge("c", "e") to 1
        )
    }

    @Test
    fun `edge flow on structure sample`() {
        val g = parseGraph(
            """
            l1: l2 l4 l5
            l2: l3 l4 l5 l6
            l3: l5 l6
            l4: l5 r1
            l5: l6 r2
            l6: r3
            r1: r2 r4 r5
            r2: r3 r4 r5 r6
            r3: r5 r6
            r4: r5
            r5: r6
            r6:
        """.trimIndent()
        )

        val l1 = g.edgeFlow("l1")
        l1.filterKeys {
            it in setOf(
                Edge("l4", "r1"),
                Edge("l5", "r2"),
                Edge("l6", "r3")
            )
        }.values.sum() shouldBe 6

        val totalStats = g.edgeFlow()

        totalStats.maxBy { it.value }.key shouldBe Edge("l5", "r2")
    }

    @Test
    fun `edge flow stats on full graph`() {
        val culprits =
            graph.edgeFlow(10).entries.sortedBy { it.value }.takeLast(3).map { it.key }.toSet()

        val splitGraph = graph.removeEdges(culprits)
        splitGraph.groups.size shouldBe 2
        splitGraph.splitWeight shouldBe 506202
    }

    private fun parseGraph(text: String) = WeightedGraph(
        text.lineSequence().map {
            val cfg = it.split(Regex("\\s+"))
            val module = cfg.first().dropLast(1)
            val connected = cfg.drop(1)
            module to connected
        }.toList().flatMap { (from, others) ->
            others.map { Edge(from, it, 1) }
        }.toSet()
    )

}