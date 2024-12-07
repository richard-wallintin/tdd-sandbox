package y2024.day7

import util.longs

data class Equation(
    val operands: List<Long>,
    val result: Long
) {
    fun solutions() = results(operands.reversed()).count { it == result }

    private fun results(tail: List<Long>): Sequence<Long> = sequence {
        val l = tail.firstOrNull() ?: return@sequence
        val r = tail.drop(1)
        if (r.isEmpty()) yield(l)
        else results(r).forEach {
            yield(l + it)
            yield(l * it)
        }
    }

    companion object {
        fun parse(line: String): Equation {
            val (result, operands) = line.split(':', limit = 2)
            return Equation(operands.longs(), result.toLong())
        }

        fun parseMany(text: String) = text.lineSequence().map { parse(it) }

        fun Sequence<Equation>.totalCalibrationResult() =
            filter { it.solutions() > 0 }.sumOf { it.result }
    }
}
