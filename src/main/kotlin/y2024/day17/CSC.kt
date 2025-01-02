package y2024.day17

data class CSC(
    val registerA: Long = 0,
    val registerB: Long = 0,
    val registerC: Long = 0,
    val program: List<Int> = emptyList(),
    val pointer: Int = 0,
    val output: List<Int> = emptyList(),
) {
    private fun combo(operand: Int) = when (operand) {
        in 0..3 -> operand.toLong()
        4 -> registerA
        5 -> registerB
        6 -> registerC
        else -> throw IllegalArgumentException("combo operand $operand unexpected")
    }

    fun adv(operand: Int) = copy(registerA = dv(operand))

    private fun dv(operand: Int) = registerA shr combo(operand).toInt()

    fun bxl(operand: Int) = copy(registerB = registerB xor operand.toLong())
    fun bst(operand: Int) = copy(registerB = combo(operand) and 7)

    fun jnz(operand: Int): CSC {
        return if (registerA == 0L) next()
        else copy(pointer = operand)
    }

    fun bxc() = copy(registerB = registerB xor registerC)
    fun out(operand: Int) = copy(output = output + (combo(operand).toInt() and 7))
    fun bdv(operand: Int) = copy(registerB = dv(operand))
    fun cdv(operand: Int) = copy(registerC = dv(operand))

    fun exec() = compute().last()

    private fun compute() = sequence<CSC> {
        var x: CSC? = this@CSC
        while (x != null) {
            yield(x)
            x = x.step()
        }
    }

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

    val selfCopyRegisterA: Long by lazy {
        var test = 0L
        program.indices.reversed().forEach { i ->
            // this approach relies on the fact that the program will always shift A by 3 bits
            // for every output number
            test = test shl 3
            while (copy(registerA = test).exec().output != program.drop(i))
                test++
        }
        return@lazy test
    }

    val copiesProgram: Boolean by lazy {
        compute().takeWhile {
            it.output == program.take(it.output.size)
        }.lastOrNull()?.output == program
    }
}