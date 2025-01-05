package y2024.day19

@JvmInline
value class StripePattern(private val colors: String) {
    private val length get() = colors.length

    fun canBeArranged(
        patterns: List<StripePattern>,
        cache: MutableMap<StripePattern, Long> = mutableMapOf(),
    ): Boolean {
        return recursiveCountArrangements(patterns, cache) > 0
    }

    fun countArrangements(patterns: List<StripePattern>): Long {
        return recursiveCountArrangements(patterns)
    }

    fun recursiveCountArrangements(
        patterns: List<StripePattern>,
        cache: MutableMap<StripePattern, Long> = mutableMapOf(),
    ): Long {
        return if (length == 0) 1L
        else cache[this] ?: run {
            patterns.filter {
                isPrefix(it)
            }.sumOf {
                removePrefix(it).recursiveCountArrangements(patterns, cache)
            }
        }.also { cache[this] = it }
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