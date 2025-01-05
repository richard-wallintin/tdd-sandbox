package y2024.day19

import util.Memo

@JvmInline
value class StripePattern(private val colors: String) {
    private val length get() = colors.length

    fun canBeArranged(
        patterns: List<StripePattern>,
        cache: Memo<StripePattern, Long> = Memo(),
    ) = countArrangements(patterns, cache) > 0

    fun countArrangements(
        patterns: List<StripePattern>,
        cache: Memo<StripePattern, Long> = Memo(),
    ): Long {
        return if (length == 0) 1L
        else cache.recall(this) {
            patterns.filter {
                isPrefix(it)
            }.sumOf {
                removePrefix(it).countArrangements(patterns, cache)
            }
        }
    }

    private fun removePrefix(p: StripePattern) = StripePattern(colors.removePrefix(p.colors))
    private fun isPrefix(prefix: StripePattern): Boolean = colors.startsWith(prefix.colors)

    override fun toString() = colors

    companion object {
        fun parseMany(line: String): List<StripePattern> {
            return line.split(Regex(", ")).map { StripePattern(it) }
        }

        fun parseLines(lines: Sequence<String>) = lines.map { StripePattern(it) }
    }
}