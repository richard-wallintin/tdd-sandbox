package y2023.day20

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PulseTest {

    private val sampleInput = """
        broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a
    """.trimIndent()

    private val sampleNetwork = Network.setup(sampleInput)

    class TestModule(var moduleState: Module) {
        fun receive(pulse: Pulse) = moduleState.receive(pulse).let { (newState, output) ->
            moduleState = newState
            output
        }

        fun add(input: String) {
            moduleState = moduleState.add(input)
        }
    }


    @Test
    fun `flip flop module`() {
        val ffm = TestModule(FlipFlopModule(name = "a", connect = listOf("b")))

        ffm.receive(Pulse.high("?", "a")) shouldBe emptyList()
        ffm.receive(Pulse.low("?", "a")) shouldBe listOf(Pulse.high("a", "b"))
        ffm.receive(Pulse.high("?", "a")) shouldBe emptyList()
        ffm.receive(Pulse.low("?", "a")) shouldBe listOf(Pulse.low("a", "b"))
    }

    @Test
    fun `conjunction module single input`() {
        val cm = TestModule(ConjunctionModule(name = "x", connect = listOf("y")))
        cm.add(input = "i")

        cm.receive(Pulse.high("i", "x")) shouldBe listOf(Pulse.low("x", "y"))
        cm.receive(Pulse.low("i", "x")) shouldBe listOf(Pulse.high("x", "y"))
    }

    @Test
    fun `conjunction module multiple inputs`() {
        val cm = TestModule(ConjunctionModule(name = "x", connect = listOf("y")))
        cm.add(input = "i")
        cm.add(input = "j")

        cm.receive(Pulse.high("i", "x")) shouldBe listOf(Pulse.high("x", "y"))
        cm.receive(Pulse.high("j", "x")) shouldBe listOf(Pulse.low("x", "y"))
    }

    @Test
    fun broadcaster() {
        val bm = TestModule(BroadcasterModule(connect = listOf("a", "b", "c")))

        bm.receive(Pulse.high("?", "broadcaster")) shouldBe listOf(
            Pulse.high("broadcaster", "a"),
            Pulse.high("broadcaster", "b"),
            Pulse.high("broadcaster", "c")
        )
    }

    @Test
    fun `configure network reference sample`() {

        sampleNetwork.pushButton().joinToString("\n") shouldBe """
            button -low-> broadcaster
            broadcaster -low-> a
            broadcaster -low-> b
            broadcaster -low-> c
            a -high-> b
            b -high-> c
            c -high-> inv
            inv -low-> a
            a -low-> b
            b -low-> c
            c -low-> inv
            inv -high-> a
        """.trimIndent()
    }

    @Test
    fun `determine cycle`() {
        sampleNetwork.cyclePulseValue() shouldBe 32000000

        val secondSample = Network.setup(
            """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
        """.trimIndent()
        )
        secondSample.cyclePulseValue() shouldBe 11687500
    }

    @Test
    fun `part 1`() {
        Network.setup(AOC.getInput("/2023/day20.txt")).cyclePulseValue() shouldBe 684125385
    }
}