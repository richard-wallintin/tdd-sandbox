package day8

import AOC
import day8.Direction.Companion.navigationInstructions
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigInteger

class DesertTest {


    private val aaa = Node(
        id = "AAA",
        left = "BBB",
        right = "CCC"
    )

    private val bbb = Node(
        id = "BBB",
        left = "DDD",
        right = "EEE"
    )

    private val referenceInput = """
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent()

    @Test
    fun `node navigation`() {
        Network(listOf(aaa, bbb)).navigate("AAA", Direction.L) shouldBe bbb
    }

    @Test
    fun `parse node`() {
        Node.of("AAA = (BBB, CCC)") shouldBe aaa
    }

    @Test
    fun `parse directions`() {
        referenceInput.lineSequence().first().navigationInstructions() shouldBe listOf(
            Direction.R, Direction.L
        )
    }

    @Test
    fun `navigate multiple steps`() {
        val (network, directions) = parseNetworkAndDirections(referenceInput)

        network.navigate("AAA", directions).id shouldBe "ZZZ"
    }

    private fun parseNetworkAndDirections(spec: String): Pair<Network, List<Direction>> {
        val directions = spec.lineSequence().first().navigationInstructions()

        val network =
            spec.lineSequence().drop(2).map { Node.of(it) }.toList().let { Network(it) }
        return Pair(network, directions)
    }

    @Test
    fun `find zzz`() {
        val (network, directions) = parseNetworkAndDirections(
            """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()
        )

        network.find("AAA", "ZZZ", directions) shouldBe 6
    }

    private val aocInput = AOC.getInput("/day8.txt")

    @Test
    fun `part one`() {
        val (n, d) = parseNetworkAndDirections(aocInput)

        n.find("AAA", "ZZZ", d) shouldBe 21_251
    }

    private val startNode: NodeSelector = { it.id.endsWith("A") }
    private val endNode: NodeSelector = { it.id.endsWith("Z") }

    private val referenceInput2 = """
                LR
    
                11A = (11B, XXX)
                11B = (XXX, 11Z)
                11Z = (11B, XXX)
                22A = (22B, XXX)
                22B = (22C, 22C)
                22C = (22Z, 22Z)
                22Z = (22B, 22B)
                XXX = (XXX, XXX)
            """.trimIndent()

    @Test
    fun `simultaneous navigation`() {
        val (n, d) = parseNetworkAndDirections(referenceInput2)

        n.navigate(startNode, d).map(Node::id) shouldBe listOf("11Z", "22C")
        n.find(startNode, endNode, d) shouldBe BigInteger.valueOf(6)
    }

    @Test
    fun `part 2`() {
        val (n, d) = parseNetworkAndDirections(aocInput)
        n.find(startNode, endNode, d) shouldBe BigInteger("11678319315857")

    }

    @Test
    fun `find path length to endNodes`() {
        val (n, d) = parseNetworkAndDirections(aocInput)

        n.find("DPA", endNode, d) shouldBe 16409L
        n.find("GGZ", endNode, d) shouldBe 16409L

        n.find("JPA", endNode, d) shouldBe 11567L
        n.find("MCZ", endNode, d) shouldBe 11567L

        n.find("DBA", endNode, d) shouldBe 18023
        n.find("TPZ", endNode, d) shouldBe 18023L
    }
}

