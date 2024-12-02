package y2024

import util.integers
import kotlin.math.abs

data class Report(val levels: List<Int>) {
    private val deltas = levels.zipWithNext { a, b -> b - a }

    val safe = (allIncreasing() || allDecreasing()) &&
            deltas.map(::abs).all { it <= 3 }

    private fun allDecreasing() = deltas.all { it < 0 }

    private fun allIncreasing() = deltas.all { it > 0 }

    companion object {
        fun parse(text: String): List<Report> {
            return text.lineSequence().filter { it.isNotBlank() }.map {
                Report(it.integers())
            }.toList()
        }
    }

}
