package y2024.day17

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

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

    private val inputComputer = CSC(
        registerA = 18427963,
        program = listOf(
            2, 4, // bst: B = A % 8
            1, 1, // bxl: B = B xor 1
            7, 5, // cdv: C = A / 2^B = A >> B
            0, 3, // adv: A = A / 2^3 = A >> 3
            4, 3, // bxc: B = B xor C
            1, 6, // bxl: B = B xor 6
            5, 5, // out: output(B % 8)
            3, 0 // jnz - 0 start again if A > 0
        )
    )

    @Test
    fun part1() {
        inputComputer.exec().outputAsString shouldBe
                "2,0,7,3,0,3,1,3,7"
    }

    @Test
    fun `self-reproducing example`() {
        val selfReproducing = CSC(
            registerA = 117440,
            program = listOf(0, 3, 5, 4, 3, 0)
        )
        selfReproducing.exec().outputAsString shouldBe "0,3,5,4,3,0"
        selfReproducing.copiesProgram shouldBe true
    }

    @Test
    fun `compute ideal register-a value`() {
        CSC(
            program = listOf(0, 3, 5, 4, 3, 0)
        ).selfCopyRegisterA shouldBe 117440
    }

    @Test
    fun part2() {
        inputComputer.selfCopyRegisterA shouldBe 247839539763386L
    }
}
