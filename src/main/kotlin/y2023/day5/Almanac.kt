package y2023.day5

import util.chunkedBy
import util.longs

data class Almanac(
    val seeds: Sequence<Long>,
    val mappings: List<Mapping>
) {

    val locations by lazy {
        seeds.map { mapSeedToLocation(it) }
    }

    private fun mapSeedToLocation(seed: Long) = mappings.fold(seed) { i, m ->
        m.map(i)
    }

    companion object {
        fun read(text: String, seedRanges: Boolean = false): Almanac {
            val seq = text.lineSequence().chunkedBy { it.isBlank() }.iterator()

            val seedLine = seq.next().first()
            return Almanac(
                seeds = if(seedRanges) seedLine.seedRanges() else seedLine.seeds(),
                mappings = seq.asSequence().map { Mapping.parse(it) }.toList()
            )
        }

        fun String.seeds() = lines().first().substring(6).longs().asSequence()
        fun String.seedRanges() = seeds().chunked(2).map { (start, len) ->
            (start until start + len).asSequence()
        }.reduce(Sequence<Long>::plus)
    }
}
