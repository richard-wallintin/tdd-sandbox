package y2022.day10

sealed interface Instruction {
    companion object {
        fun parse(line: String): Instruction {
            return if (line == "noop")
                Noop
            else if (line.startsWith("addx"))
                AddX(line.substring(5).toInt(10))
            else
                throw IllegalArgumentException("no instruction `$line`")
        }
    }
}

data object Noop : Instruction
data class AddX(val amount: Int) : Instruction
