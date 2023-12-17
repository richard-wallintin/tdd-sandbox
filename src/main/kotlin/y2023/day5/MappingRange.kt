package y2023.day5

data class MappingRange(val destination: Long, val source: Long, val len: Long) {
    private val offset = destination - source
    private val sourceRange = source until (source + len)

    fun map(src: Long) = if (src in sourceRange) src + offset else null
}
