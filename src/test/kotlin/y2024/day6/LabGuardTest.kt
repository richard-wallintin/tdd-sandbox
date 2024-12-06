package y2024.day6

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection
import util.Point
import util.repeat

class LabGuardTest {

    private val sampleMap = LabMap.parse(
        """
                ....#.....
                .........#
                ..........
                ..#.......
                .......#..
                ..........
                .#..^.....
                ........#.
                #.........
                ......#...
            """.trimIndent()
    )

    @Test
    fun `parse map and get guard position`() {
        sampleMap.size shouldBe Point(10, 10)
        sampleMap.guard shouldBe Point(4, 6)
        sampleMap.guardDirection shouldBe CardinalDirection.N
    }

    @Test
    fun `let guard step`() {
        sampleMap.step().guard shouldBe Point(4, 5)
        sampleMap.repeat(5, LabMap::step).guard shouldBe Point(4, 1)
    }

    @Test
    fun `guard turns right at obstacle`() {
        val after6steps = sampleMap.repeat(6, LabMap::step)
        after6steps.guard shouldBe Point(4, 1)
        after6steps.guardDirection shouldBe CardinalDirection.E
        val after7steps = after6steps.step()
        after7steps.guard shouldBe Point(5, 1)
    }

    @Test
    fun `guard leaves map`() {
        sampleMap.repeat(100, LabMap::step).guardIsGone shouldBe true
    }

    @Test
    fun `collect all positions until guard leaves map`() {
        val allPositions = sampleMap.allGuardPositions()

        allPositions.size shouldBe 41
    }

    @Test
    fun part1() {
        LabMap.parse(AOC.getInput("/2024/day6.txt")).allGuardPositions().size shouldBe 5404
    }
}
