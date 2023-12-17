package y2023.day6

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2023.day6.Boat.Companion.margin
import y2023.day6.Boat.Companion.size


class BoatRaceTest {

    val boat = Boat(remainingTime = 7)

    @Test
    fun `basic examples`() {

        boat.push(0).speed shouldBe 0

        val push1 = boat.push(1)
        push1.speed shouldBe 1
        push1.distance shouldBe 6

        boat.push(2).distance shouldBe 10
        boat.push(3).distance shouldBe 12
        boat.push(4).distance shouldBe 12
        boat.push(5).distance shouldBe 10
        boat.push(6).distance shouldBe 6
        boat.push(7).distance shouldBe 0
    }

    @Test
    fun `compute push time`() {
        boat.pushTime(9).first shouldBe 2
        boat.pushTime(9).last shouldBe 5

        val boat15 = Boat(remainingTime = 15)

        boat15.pushTime(40).first shouldBe 4
        boat15.pushTime(40).last shouldBe 11
        boat15.pushTime(40).size shouldBe 8

        Boat(remainingTime = 30).pushTime(200).size shouldBe 9
    }

    private val referenceInput = """
                    Time:      7  15   30
                    Distance:  9  40  200
                """.trimIndent()

    private val aocInput = AOC.getInput("/2023/day6.txt")

    @Test
    fun parseRaces() {
        margin(referenceInput) shouldBe 288
    }

    @Test
    fun `part 1`() {
        margin(aocInput) shouldBe 220_320
    }

    @Test
    fun `parse single race`() {
        margin(referenceInput, true) shouldBe 71_503
    }

    @Test
    fun `part 2`() {
        margin(aocInput, true) shouldBe 34_454_850L
    }
}