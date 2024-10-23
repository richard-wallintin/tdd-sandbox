package y2022.day10

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TubeTest {

    @Test
    fun `CPU has register X and a current cycle`() {
        val cpu = CPU()
        cpu.registerX shouldBe 1
        cpu.cycle shouldBe 1
    }

    @Test
    fun `can execute a program of noops`() {
        val program = Program(listOf(Noop, Noop))
        val cpu = CPU()
        val cpuDone = cpu.execute(program)

        cpuDone.registerX shouldBe 1
        cpuDone.cycle shouldBe 3
    }

    private val simpleProgram = Program(listOf(Noop, AddX(3), AddX(-5)))

    @Test
    fun `can execute simple program with addx`() {
        val done = CPU().execute(simpleProgram)

        done.cycle shouldBe 6
        done.registerX shouldBe -1
    }

    @Test
    fun `all intermediate states can be checked`() {
        CPU().trace(simpleProgram).toList() shouldBe listOf(
            CPU(1, 1),
            CPU(2, 1),
            CPU(3, 1),
            CPU(4, 4),
            CPU(5, 4),
            CPU(6, -1),
        )
    }

    @Test
    fun `signal strength`() {
        CPU(20, 1).signal shouldBe 20
    }

    @Test
    fun `parse program`() {
        Program.parse(
            """
            noop
            addx 3
            addx -5
        """.trimIndent()
        ) shouldBe simpleProgram
    }

    private val largerSample = Program.parse(AOC.getInput("2022/day10-a.txt"))

    @Test
    fun `can execute larger sample`() {
        val program = largerSample

        CPU().trace(program).take(20).last().signal shouldBe 420
        CPU().trace(program).take(60).last().signal shouldBe 1140

        CPU().signalTrace(program) shouldBe 13140
    }

    private val puzzle = Program.parse(AOC.getInput("2022/day10.txt"))

    @Test
    fun `part 1`() {
        CPU().signalTrace(
            puzzle
        ) shouldBe 15260
    }

    @Test
    fun `compute pixel lit for cpu state`() {
        val trace = CPU().trace(largerSample)
        val (c1, c2, c3) = trace.take(3).map { CRT(it) }.toList()

        c1.pixel shouldBe 0
        c1.lit shouldBe true

        c2.pixel shouldBe 1
        c2.lit shouldBe true

        c3.pixel shouldBe 2
        c3.lit shouldBe false

        trace.take(21).map { CRT(it).char }.joinToString("") { it.toString() } shouldBe
                "##..##..##..##..##..#"
    }

    @Test
    fun `get full crt output`() {
        CRT.output(largerSample) shouldBe """
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
        """.trimIndent()
    }

    @Test
    fun `part 2`() {
        // PGHFGLUG
        CRT.output(puzzle) shouldBe """
            ###...##..#..#.####..##..#....#..#..##..
            #..#.#..#.#..#.#....#..#.#....#..#.#..#.
            #..#.#....####.###..#....#....#..#.#....
            ###..#.##.#..#.#....#.##.#....#..#.#.##.
            #....#..#.#..#.#....#..#.#....#..#.#..#.
            #.....###.#..#.#.....###.####..##...###.
        """.trimIndent()
    }
}