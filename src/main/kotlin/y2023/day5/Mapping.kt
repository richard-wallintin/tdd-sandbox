package y2023.day5

import util.longs

data class Mapping(val types: Pair<String, String>, val ranges: List<MappingRange> = listOf()) {
    fun map(src: Long): Long {
        return ranges.fold(src) { default, range ->
            range.map(src) ?: default
        }
    }

    companion object {
        fun parse(text: String): Mapping {
            return parse(text.lines())
        }

        fun parse(lines: List<String>): Mapping {
            val (from, to) = Regex("(\\w+)-to-(\\w+) map:").matchEntire(lines.first())?.destructured
                ?: throw IllegalArgumentException("cant parse ${lines.first()}")

            return Mapping(from to to, lines.drop(1).map {
                val (d, s, l) = it.longs()
                MappingRange(d, s, l)
            })
        }
    }
}
