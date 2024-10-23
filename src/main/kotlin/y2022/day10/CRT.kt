package y2022.day10

data class CRT(val cpu: CPU) {

    val pixel = cpu.cycle - 1
    private val spritePosition = cpu.registerX
    private val pixelInCurrentRow = pixel % 40

    val lit = pixelInCurrentRow in (spritePosition - 1..spritePosition + 1)
    val char = if (lit) '#' else '.'

    companion object {
        fun output(program: Program): String {
            return CPU().trace(program).take(240).map { CRT(it).char }
                .chunked(40).map { line ->
                line.joinToString("") { it.toString() }
            }.joinToString("\n")
        }
    }
}
