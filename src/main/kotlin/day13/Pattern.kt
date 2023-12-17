package day13

import util.chunkedBy

data class Pattern(val matrix: List<String>) {
    val rows = matrix.size
    val columns = matrix.first().length

    fun row(idx: Int): String {
        return matrix[idx]
    }

    fun col(idx: Int): String {
        return matrix.map { it[idx] }.toCharArray().concatToString()
    }

    fun mirrorColumn(diff: Int = 0) = (1 until columns).firstOrNull {
        isMirrorColumn(it, diff)
    }

    private fun isMirrorColumn(idx: Int, diff: Int) =
        mirrorIndexPairs(idx, columns).sumOf { (l, r) ->
            col(l).diff(col(r))
        } == diff

    private fun mirrorIndexPairs(idx: Int, count: Int): List<Pair<Int, Int>> {
        val leftColumns = (0 until idx).reversed()
        val rightColumns = (idx until count)
        return leftColumns.zip(rightColumns)
    }

    fun mirrorRow(diff: Int = 0) = (1 until rows).firstOrNull {
        isMirrorRow(it, diff)
    }

    private fun isMirrorRow(idx: Int, diff: Int) = mirrorIndexPairs(idx, rows).sumOf { (l, r) ->
        row(l).diff(row(r))
    } == diff

    companion object {
        fun ofMany(text: String): Sequence<Pattern> {
            return text.lineSequence().chunkedBy { it.isBlank() }.map { Pattern(it) }
        }

        fun String.diff(o: String): Int {
            return this.zip(o).count { (a, b) -> a != b }
        }
    }

    fun summary(diff: Int = 0): Int {
        return mirrorColumn(diff) ?: mirrorRow(diff)?.let { it * 100 }
        ?: throw IllegalStateException("$this must have a mirror column or row!")
    }

}
