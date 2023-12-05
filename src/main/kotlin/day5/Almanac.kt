package day5

import util.chunkedBy
import util.longs

data class Almanac(
    val seeds: List<Long>,
    val mappings: List<Mapping>
) {

    val locations by lazy {
        seeds.map { mapSeedToLocation(it) }
    }

    fun mapSeedToLocation(seed: Long) = mappings.fold(seed) { i, m ->
        m.map(i)
    }

    companion object {
        fun read(text: String): Almanac {
            val seq = text.lineSequence().chunkedBy { it.isBlank() }.iterator()

            return Almanac(
                seeds = seq.next().first().seeds(),
                mappings = seq.asSequence().map { Mapping.parse(it) }.toList()
            )
        }

        fun String.seeds() = lines().first().substring(6).longs()
    }
}
