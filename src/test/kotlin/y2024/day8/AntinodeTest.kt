package y2024.day8

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Point

class AntinodeTest {
    private val sampleMap = AntennaMap.parse(
        """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
        """.trimIndent()
    )

    @Test
    fun `parse map`() {
        sampleMap.antennaCount shouldBe 7
        sampleMap.antennas shouldBe setOf(
            Antenna(Point(8, 1), '0'),
            Antenna(Point(5, 2), '0'),
            Antenna(Point(7, 3), '0'),
            Antenna(Point(4, 4), '0'),
            Antenna(Point(6, 5), 'A'),
            Antenna(Point(8, 8), 'A'),
            Antenna(Point(9, 9), 'A'),
        )
    }

    @Test
    fun `compute antinode locations for antenna pair`() {
        Antenna(Point(5, 2), '0').antinodes(
            Antenna(Point(7, 3), '0'),
            sampleMap.boundary
        ) shouldBe listOf(Point(9, 4), Point(3, 1))
    }

    @Test
    fun `antennas with different frequencies hace no antinodes`() {
        Antenna(Point(4, 4), '0').antinodes(
            Antenna(Point(6, 5), 'A'),
            sampleMap.boundary
        ) shouldBe emptyList()
    }

    @Test
    fun `compute all antinode locations`() {
        sampleMap.antinodeCount shouldBe 14
    }

    private val inputMap = AntennaMap.parse(AOC.getInput("/2024/day8.txt"))

    @Test
    fun part1() {
        inputMap.antinodeCount shouldBe 359
    }

    @Test
    fun `antnodeCount with enhanced formula`() {
        sampleMap.enhanced().antinodeCount shouldBe 34
    }

    @Test
    fun part2() {
        inputMap.enhanced().antinodeCount shouldBe 1293
    }
}