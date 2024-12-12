package y2024.day12

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Point.Companion.by

class FencesTest {

    private val smallSampleGarden = Garden.parse(
        """
                AAAA
                BBCD
                BBCC
                EEEC
            """.trimIndent()
    )

    @Test
    fun `find regions in a garden`() {
        smallSampleGarden shouldBe Garden(
            regions = setOf(
                Region(setOf(0 by 0, 1 by 0, 2 by 0, 3 by 0)),
                Region(setOf(0 by 1, 1 by 1, 0 by 2, 1 by 2)),
                Region(setOf(2 by 1, 2 by 2, 3 by 2, 3 by 3)),
                Region(setOf(3 by 1)),
                Region(setOf(0 by 3, 1 by 3, 2 by 3)),
            )
        )
    }

    @Test
    fun `compute region area and perimeter`() {
        val a = Region(setOf(0 by 0, 1 by 0, 2 by 0, 3 by 0))
        a.area shouldBe 4
        a.perimeter shouldBe 10

        val b = Region(setOf(0 by 1, 1 by 1, 0 by 2, 1 by 2))
        b.perimeter shouldBe 8

        Region(setOf(3 by 1)).perimeter shouldBe 4
    }

    private val xoSample = Garden.parse(
        """
                OOOOO
                OXOXO
                OOOOO
                OXOXO
                OOOOO
            """.trimIndent()
    )

    @Test
    fun `regions contained in other regions`() {
        xoSample.regions.size shouldBe 5
    }

    private val largeSample = Garden.parse(
        """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()
    )

    @Test
    fun totalPrice() {
        smallSampleGarden.totalPrice shouldBe 140
        xoSample.totalPrice shouldBe 772
        largeSample.totalPrice shouldBe 1930
    }

    @Test
    fun part1() {
        Garden.parse(AOC.getInput("/2024/day12.txt")).totalPrice shouldBe 1363484
    }
}