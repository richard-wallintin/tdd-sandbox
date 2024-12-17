package y2024.day14

import AOC
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import util.Point
import util.Point.Companion.by
import util.forever

class RobotTest {

    private val sampleRobot =
        Robot(position = Point(2, 4), velocity = Point(2, -3))

    private val sampleRobots = Robot.parseMany(
        """
                p=0,4 v=3,-3
                p=6,3 v=-1,-3
                p=10,3 v=-1,2
                p=2,0 v=2,-1
                p=0,0 v=1,3
                p=3,0 v=-2,-2
                p=7,6 v=-1,-3
                p=3,0 v=-1,-2
                p=9,3 v=2,3
                p=7,3 v=-1,2
                p=2,4 v=2,-3
                p=9,5 v=-3,-3
            """.trimIndent()
    ).toList()

    private val sampleFloor = Floor(robots = sampleRobots, size = Point(11, 7))

    @Test
    fun `robot positioning`() {
        sampleRobot.move(5, floorSize = sampleFloor.size).position shouldBe Point(1, 3)
    }

    @Test
    fun `parse robot`() {
        Robot.parse("p=2,4 v=2,-3") shouldBe sampleRobot
    }

    @Test
    fun `parse sample robots`() {
        sampleRobots shouldHaveSize 12
    }

    @Test
    fun `count robots in quadrants`() {
        sampleFloor.after(100).safetyFactor shouldBe 12
    }

    private val inputFloor = Floor(
        size = Point(101, 103),
        robots = Robot.parseMany(AOC.getInput("2024/day14.txt")).toList()
    )

    @Test
    fun part1() {
        inputFloor.after(100).safetyFactor shouldBe 224438715L
    }

    @Test
    fun `render floor`() {
        sampleFloor.render() shouldBe """
            1.12.......
            ...........
            ...........
            ......11.11
            1.1........
            .........1.
            .......1...
        """.trimIndent()
    }

    @Test @Disabled("dead slow, 3 minutes")
    fun part2() {
        inputFloor.forever { after(1) }.take(10_000).mapIndexedNotNull {  index, floor ->
            val rendered = floor.render(" ") { "X" }
            if(rendered.contains("XXXXXXXX")) {
                println("=== $index ===")
                println(rendered)
                index
            } else null
        }.first() shouldBe 7603
    }
}

data class Floor(val robots: List<Robot>, val size: Point) {
    val safetyFactor: Long by lazy {
        val quadrantCounts = robots.groupingBy { quadrant(it.position) }.eachCount()
            .filterKeys { it != null }
        quadrantCounts.values.map { it.toLong() }.reduce(Long::times)
    }

    private val pivotX = size.x / 2
    private val pivotY = size.y / 2

    private fun quadrant(position: Point): Int? {
        return if (position.x < pivotX) {
            if (position.y < pivotY) 1
            else if (position.y > pivotY) 2
            else null
        } else if (position.x > pivotX) {
            if (position.y < pivotY) 3
            else if (position.y > pivotY) 4
            else null
        } else null
    }

    fun after(seconds: Int) = copy(robots = robots.map { it.move(seconds, size) })
    fun render(empty: String = ".", present: (Int) -> String = { it.toString().take(1) }): String {
        return (0..<size.y).joinToString("\n") { y ->
            (0..<size.x).joinToString("") { x ->
                val count = robots.count { it.position == x by y }
                if (count == 0) empty else present(count)
            }
        }
    }
}

data class Robot(val position: Point, val velocity: Point) {
    fun move(seconds: Int, floorSize: Point) = copy(
        position = Point(
            clip(position.x + velocity.x * seconds, floorSize.x),
            clip(position.y + velocity.y * seconds, floorSize.y)
        )
    )

    private fun clip(v: Long, limit: Long) = (v % limit).let { if (it < 0) limit + it else it }

    companion object {
        fun parse(line: String): Robot {
            val values =
                Regex("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)").matchEntire(line)!!.groupValues
            return Robot(
                position = Point(values[1].toLong(), values[2].toLong()),
                velocity = Point(values[3].toLong(), values[4].toLong())
            )
        }

        fun parseMany(text: String): Sequence<Robot> = text.lineSequence().map { parse(it) }
    }
}
