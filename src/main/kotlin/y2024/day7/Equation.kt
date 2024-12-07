package y2024.day7

import util.longs

data class Equation(
    val operands: List<Long>,
    val result: Long
) {
    fun solutions(concat: Boolean = false) =
        results(operands.reversed(), concat).count { it == result }

    private fun results(head: List<Long>, concat: Boolean = false): Sequence<Long> = sequence {
        val right = head.firstOrNull() ?: return@sequence
        val left = head.drop(1)
        if (left.isEmpty()) yield(right)
        else results(left, concat).forEach { leftResult ->
            yield(right + leftResult)
            yield(right * leftResult)
            if (concat) yield((leftResult.toString() + right.toString()).toLong())
        }
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
