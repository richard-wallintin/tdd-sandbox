package y2022.day10

private val interestingCycles = setOf(20, 60, 100, 140, 180, 220)

data class CPU(val cycle: Int = 1, val registerX: Int = 1) {

    val signal = cycle * registerX

    fun execute(program: Program = Program()) = trace(program).last()

    fun trace(program: Program) = sequence {
        var cycle = this@CPU.cycle
        var registerX = this@CPU.registerX

        program.forEach { i ->
            when (i) {
                is AddX -> {
                    yield(CPU(cycle++, registerX))
                    yield(CPU(cycle++, registerX))
                    registerX += i.amount
                }

                else -> {
                    yield(CPU(cycle++, registerX))
                }
            }
        }
        yield(CPU(cycle, registerX))
    }

    fun signalTrace(program: Program): Int {
        return trace(program)
            .take(interestingCycles.max())
            .filter { it.cycle in interestingCycles }
            .sumOf { it.signal }
    }
}
