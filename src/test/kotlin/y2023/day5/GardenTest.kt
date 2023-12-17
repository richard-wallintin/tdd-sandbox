package y2023.day5

import AOC
import y2023.day5.Almanac.Companion.seedRanges
import y2023.day5.Almanac.Companion.seeds
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import y2023.day5.Almanac
import y2023.day5.Mapping
import y2023.day5.MappingRange

class GardenTest {

    @Test
    fun `mapping function`() {
        val m = MappingRange(destination = 50, source = 98, len = 2)

        m.map(98) shouldBe 50
        m.map(99) shouldBe 51
        m.map(100) shouldBe null
        m.map(97) shouldBe null
    }

    @Test
    fun `mapping example 2`() {
        val m = MappingRange(52, 50, 48)

        m.map(50) shouldBe 52
        m.map(96) shouldBe 98
    }

    @Test
    fun `full mapping`() {
        val m = Mapping(
            types = "seed" to "soil",
            ranges = listOf(
                MappingRange(destination = 50, source = 98, len = 2),
                MappingRange(destination = 52, source = 50, len = 48)
            )
        )

        m.map(0) shouldBe 0
        m.map(1) shouldBe 1

        m.map(48) shouldBe 48
        m.map(49) shouldBe 49
        m.map(50) shouldBe 52
        m.map(51) shouldBe 53

        m.map(96) shouldBe 98
        m.map(97) shouldBe 99
        m.map(98) shouldBe 50
        m.map(99) shouldBe 51
    }

    @Test
    fun `parse a mapping`() {
        Mapping.parse(
            """
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
        """.trimIndent()
        ) shouldBe Mapping(
            "soil" to "fertilizer", listOf(
                MappingRange(0, 15, 37),
                MappingRange(37, 52, 2),
                MappingRange(39, 0, 15),
            )
        )
    }

    @Test
    fun `read items`() {
        referenceData.seeds().toList() shouldBe listOf(79, 14, 55, 13)
    }

    @Test
    fun `read seeds and all mappings (almanac)`() {
        val almanac = Almanac.read(referenceData)
        almanac.seeds.toList() shouldBe listOf(79, 14, 55, 13)
        almanac.mappings shouldHaveSize 7
        almanac.locations.toList() shouldBe listOf(82, 43, 86, 35)
        almanac.locations.min() shouldBe 35
    }

    private val aocInput = AOC.getInput("/2023/day5.txt")

    @Test
    fun `part 1`() {
        Almanac.read(aocInput).locations.min() shouldBe 199602917L
    }

    @Test
    fun seedRanges() {
        "seeds: 79 14 55 13".seedRanges().toList() shouldBe (
                79..92
                ) + (55..67)
    }

    @Test
    fun `part 2 reference`() {
        Almanac.read(referenceData, seedRanges = true).locations.min() shouldBe 46
    }

    @Test @Disabled("very slow test, takes ca. 8 minutes to execute")
    fun `part 2 final`() {
        Almanac.read(aocInput, seedRanges = true).locations.min() shouldBe 2_254_686L
    }

    private val referenceData = """
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()
}