package y2024.day7

import util.longs

data class Equation(
    val operands: List<Long>,
    val result: Long
) {
    fun solutions(concat: Boolean = false) =
        results(operands.first(), operands.drop(1), concat).count { it == result }

    private fun results(left: Long, tail: List<Long>, concat: Boolean): Sequence<Long> {
        if (tail.isEmpty()) return sequenceOf(left)

        val right = tail.first()
        val remainder = tail.drop(1)

        return results(left + right, remainder, concat) +
                results(left * right, remainder, concat) +
                if (concat)
                    results(
                        (left.toString() + right.toString()).toLong(),
                        remainder,
                        true
                    ) else emptySequence()
    }

    companion object {
        fun parse(line: String): Equation {
            val (result, operands) = line.split(':', limit = 2)
            return Equation(operands.longs(), result.toLong())
        }

        fun parseMany(text: String) = text.lineSequence().map { parse(it) }

        fun Sequence<Equation>.totalCalibrationResult(concat: Boolean = false) =
            filter { it.solutions(concat) > 0 }.sumOf { it.result }
    }
}
