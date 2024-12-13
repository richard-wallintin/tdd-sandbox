package y2024.day13

import AOC
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Point
import util.Point.Companion.by

class ClawMachineTest {

    private val machine1 = ClawMachine(
        buttonA = Point(94, 34),
        buttonB = Point(22, 67),
        prize = Point(8400, 5400)
    )

    @Test
    fun `simulate claw machine moves`() {
        // Button A: X+94, Y+34
        //Button B: X+22, Y+67
        //Prize: X=8400, Y=5400
        machine1.whereAmI(movesA = 5, movesB = 7) shouldBe Point(624, 639)
        machine1.whereAmI(movesA = 80, movesB = 40) shouldBe machine1.prize
    }

    //Button A: X+26, Y+66
    //Button B: X+67, Y+21
    //Prize: X=12748, Y=12176
    private val machine2 =
        ClawMachine(buttonA = 26 by 66, buttonB = 67 by 21, prize = 12748 by 12176)

    // Button A: X+17, Y+86
    //Button B: X+84, Y+37
    //Prize: X=7870, Y=6450
    private val machine3 = ClawMachine(buttonA = 17 by 86, buttonB = 84 by 37, prize = 7870 by 6450)

    private val machineX = ClawMachine(buttonA = 10 by 10, buttonB = 1 by 1, prize = 50 by 50)

    @Test
    fun `compute all combinations towards prize`() {
        machine1.whichMovesToWin().toList() shouldBe listOf(Pair(80, 40))
        machine2.whichMovesToWin().toList() shouldBe emptyList()
        machine3.whichMovesToWin().toList() shouldBe listOf(38 to 86)
        machineX.whichMovesToWin().toList() shouldContain (5 to 0)
    }

    @Test
    fun `find cheapest moves`() {
        machineX.cheapestSolutionCosts shouldBe 15
        machine1.cheapestSolutionCosts shouldBe 280
        machine2.cheapestSolutionCosts shouldBe 0
    }

    @Test
    fun `parse many machines`() {
        ClawMachine.parseMany(
            """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176
        """.trimIndent()
        ) shouldBe listOf(machine1, machine2)
    }

    @Test
    fun part1() {
        ClawMachine.parseMany(AOC.getInput("/2024/day13.txt")).sumOf {
            it.cheapestSolutionCosts
        } shouldBe 32067
    }
}

data class ClawMachine(val buttonA: Point, val buttonB: Point, val prize: Point) {
    val cheapestSolutionCosts: Int by lazy {
        whichMovesToWin().map { (a, b) -> a * 3 + b * 1 }.minOrNull() ?: 0
    }

    fun whereAmI(movesA: Int, movesB: Int): Point {
        return Point(
            buttonA.x * movesA + buttonB.x * movesB,
            buttonA.y * movesA + buttonB.y * movesB
        )
    }

    fun whichMovesToWin(): Sequence<Pair<Int, Int>> = sequence {
        (0..100).forEach { movesA ->
            (0..100).forEach { movesB ->
                if (whereAmI(movesA, movesB) == prize)
                    yield(movesA to movesB)
            }
        }
    }

    companion object {
        fun parseMany(text: String): List<ClawMachine> {
            return Regex(
                "Button A: X\\+(\\d+), Y\\+(\\d+)\n" +
                        "Button B: X\\+(\\d+), Y\\+(\\d+)\n" +
                        "Prize: X=(\\d+), Y=(\\d+)", RegexOption.MULTILINE
            ).findAll(text).map { match ->
                val v = match.groupValues
                ClawMachine(
                    buttonA = Point(v[1].toInt(), v[2].toInt()),
                    buttonB = Point(v[3].toInt(), v[4].toInt()),
                    prize = Point(v[5].toInt(), v[6].toInt())
                )
            }.toList()
        }
    }
}
