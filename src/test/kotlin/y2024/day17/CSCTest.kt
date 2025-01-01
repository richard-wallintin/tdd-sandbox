package y2024.day17

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.power

class CSCTest {


    @Test
    fun adv() {
        CSC(registerA = 20).adv(0) shouldBe CSC(registerA = 20)
        CSC(registerA = 20).adv(1) shouldBe CSC(registerA = 10)
        CSC(registerA = 20).adv(2) shouldBe CSC(registerA = 5)
        CSC(registerA = 20).adv(3) shouldBe CSC(registerA = 2)
        CSC(registerA = 20).adv(4) shouldBe CSC(registerA = 0)
        CSC(registerA = 20, registerB = 2).adv(5) shouldBe CSC(registerA = 5, registerB = 2)
        CSC(registerA = 20, registerC = 1).adv(6) shouldBe CSC(registerA = 10, registerC = 1)
    }

    @Test
    fun bxl() {
        CSC(registerB = 7).bxl(0) shouldBe CSC(registerB = 7)
        CSC(registerB = 7).bxl(1) shouldBe CSC(registerB = 6)
    }

    @Test
    fun bst() {
        CSC().bst(3) shouldBe CSC(registerB = 3)
        CSC(registerA = 8).bst(4) shouldBe CSC(registerA = 8, registerB = 0)
    }

    @Test
    fun jnz() {
        CSC().jnz(99) shouldBe CSC(pointer = 2)
        CSC(registerA = 1).jnz(95) shouldBe CSC(registerA = 1, pointer = 95)
    }

    @Test
    fun bxc() {
        CSC(registerB = 5, registerC = 2).bxc() shouldBe CSC(registerB = 7, registerC = 2)
    }

    @Test
    fun out() {
        CSC(registerC = 10).out(6) shouldBe CSC(registerC = 10, output = listOf(2))
    }

    @Test
    fun bdv_cdv() {
        CSC(registerA = 20).bdv(2) shouldBe CSC(registerA = 20, registerB = 5)
        CSC(registerA = 20).cdv(2) shouldBe CSC(registerA = 20, registerC = 5)
    }

    @Test
    fun examples() {
        // If register C contains 9, the program 2,6 would set register B to 1.
        CSC(registerC = 9, program = listOf(2, 6)).exec().registerB shouldBe 1

        // If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
        CSC(registerA = 10, program = listOf(5, 0, 5, 1, 5, 4)).exec().output shouldBe listOf(
            0,
            1,
            2
        )

        // If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
        val result = CSC(registerA = 2024, program = listOf(0, 1, 5, 4, 3, 0)).exec()
        result.output shouldBe listOf(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0)
        result.registerA shouldBe 0

        //If register B contains 29, the program 1,7 would set register B to 26.
        CSC(registerB = 29, program = listOf(1, 7)).exec().registerB shouldBe 26

        //If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.
        CSC(
            registerB = 2024,
            registerC = 43690,
            program = listOf(4, 0)
        ).exec().registerB shouldBe 44354
    }

    @Test
    fun `sample output`() {
        CSC(
            registerA = 729,
            registerB = 0,
            registerC = 0,

            program = listOf(0, 1, 5, 4, 3, 0)
        ).exec().outputAsString shouldBe
                "4,6,3,5,6,3,5,2,1,0"
    }

    @Test
    fun part1() {
        //Register A: 18427963
        //Register B: 0
        //Register C: 0
        //
        //Program: 2,4,1,1,7,5,0,3,4,3,1,6,5,5,3,0
        CSC(
            registerA = 18427963,
            program = listOf(2, 4, 1, 1, 7, 5, 0, 3, 4, 3, 1, 6, 5, 5, 3, 0)
        ).exec().outputAsString shouldBe
                "2,0,7,3,0,3,1,3,7"
    }
}

data class CSC(
    val registerA: Int = 0,
    val registerB: Int = 0,
    val registerC: Int = 0,
    val program: List<Int> = emptyList(),
    val pointer: Int = 0,
    val output: List<Int> = emptyList(),
) {
    private fun combo(operand: Int) = when (operand) {
        in 0..3 -> operand
        4 -> registerA
        5 -> registerB
        6 -> registerC
        else -> throw IllegalArgumentException("combo operand $operand unexpected")
    }

    fun adv(operand: Int) = copy(registerA = dv(operand))

    private fun dv(operand: Int) = registerA / 2.power(combo(operand))

    fun bxl(operand: Int) = copy(registerB = registerB xor operand)
    fun bst(operand: Int) = copy(registerB = combo(operand) % 8)

    fun jnz(operand: Int): CSC {
        return if (registerA == 0) next()
        else copy(pointer = operand)
    }

    fun bxc() = copy(registerB = registerB xor registerC)
    fun out(operand: Int) = copy(output = output + (combo(operand) % 8))
    fun bdv(operand: Int) = copy(registerB = dv(operand))
    fun cdv(operand: Int) = copy(registerC = dv(operand))

    fun exec() = sequence<CSC> {
        var x: CSC? = this@CSC
        while (x != null) {
            yield(x)
            x = x.step()
        }
    }.last()

    private fun step(): CSC? {
        if (pointer in program.indices) {
            val opcode = program[pointer]
            val operand = program[pointer + 1]
            return exec(opcode, operand)
        } else return null
    }

    private fun exec(opcode: Int, operand: Int): CSC {
        return when (opcode) {
            0 -> adv(operand).next()
            1 -> bxl(operand).next()
            2 -> bst(operand).next()
            3 -> jnz(operand)
            4 -> bxc().next()
            5 -> out(operand).next()
            6 -> bdv(operand).next()
            7 -> cdv(operand).next()
            else -> throw IllegalArgumentException("opcode $opcode unsupported")
        }
    }

    private fun next(): CSC = copy(pointer = pointer + 2)

    val outputAsString by lazy { output.joinToString(",") }
}
