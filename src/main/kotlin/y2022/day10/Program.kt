package y2022.day10

data class Program(val instructions: List<Instruction> = emptyList()) {
    fun next(): Pair<Instruction?, Program> {
        return if (instructions.isEmpty()) null to this
        else instructions.first() to Program(instructions.drop(1))
    }

    inline fun forEach(action: (Instruction) -> Unit) {
        instructions.forEach(action)
    }

    companion object {
        fun parse(lines: String) =
            Program(lines.lineSequence().map(Instruction.Companion::parse).toList())
    }
}
