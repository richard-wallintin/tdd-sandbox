package y2024.day12

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection
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

    private val regionA = Region(setOf(0 by 0, 1 by 0, 2 by 0, 3 by 0))
    private val regionB = Region(setOf(0 by 1, 1 by 1, 0 by 2, 1 by 2))
    private val regionC = Region(setOf(2 by 1, 2 by 2, 3 by 2, 3 by 3))
    private val regionD = Region(setOf(3 by 1))
    private val regionE = Region(setOf(0 by 3, 1 by 3, 2 by 3))

    @Test
    fun `find regions in a garden`() {
        smallSampleGarden shouldBe Garden(
            regions = setOf(
                regionA,
                regionB,
                regionC,
                regionD,
                regionE,
            )
        )
    }

    @Test
    fun `compute region area and perimeter`() {
        this.regionA.area shouldBe 4
        this.regionA.perimeter shouldBe 10

        regionB.perimeter shouldBe 8

        regionD.perimeter shouldBe 4
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

    private val inputGarden = Garden.parse(AOC.getInput("/2024/day12.txt"))

    @Test
    fun part1() {
        inputGarden.totalPrice shouldBe 1363484
    }

    @Test
    fun `enumerate all fence segments`() {
        regionC.fences.toSet() shouldBe setOf(
            Region.Fence(2 by 1, CardinalDirection.W),
            Region.Fence(2 by 1, CardinalDirection.N),
            Region.Fence(2 by 1, CardinalDirection.E),
            Region.Fence(2 by 2, CardinalDirection.W),
            Region.Fence(2 by 2, CardinalDirection.S),
            Region.Fence(3 by 2, CardinalDirection.N),
            Region.Fence(3 by 2, CardinalDirection.E),
            Region.Fence(3 by 3, CardinalDirection.E),
            Region.Fence(3 by 3, CardinalDirection.W),
            Region.Fence(3 by 3, CardinalDirection.S),
        )
    }

    @Test
    fun `fence merge`() {
        Region.Fence(2 by 1, CardinalDirection.W).extends(
            Region.Fence(2 by 2, CardinalDirection.W),
        ) shouldBe true
        Region.Fence(1 by 1, CardinalDirection.E).extends(
            Region.Fence(2 by 2, CardinalDirection.W),
        ) shouldBe false

        Region.Fence(2 by 1, CardinalDirection.E).extends(
            Region.Fence(3 by 2, CardinalDirection.E),
        ) shouldBe false
        Region.Fence(3 by 1, CardinalDirection.E).extends(
            Region.Fence(3 by 3, CardinalDirection.E),
        ) shouldBe false
    }

    @Test
    fun `region sides computation`() {
        regionA.sides shouldBe 4
        regionC.sides shouldBe 8
    }

    @Test
    fun `discount price`() {
        smallSampleGarden.totalDiscountedPrice shouldBe 80
        xoSample.totalDiscountedPrice shouldBe 436

        Garden.parse("""
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
        """.trimIndent()).totalDiscountedPrice shouldBe 236

        Garden.parse("""
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
        """.trimIndent()).totalDiscountedPrice shouldBe 368

        largeSample.totalDiscountedPrice shouldBe 1206
    }

    @Test
    fun part2() {
        inputGarden.totalDiscountedPrice shouldBe 838988
    }
}