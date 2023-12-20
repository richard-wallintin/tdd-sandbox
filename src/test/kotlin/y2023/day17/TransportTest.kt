package y2023.day17

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection.*
import util.Point
import util.RelativeDirection.*

class TransportTest {

    private val referenceCity = City.of(
        """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent()
    )

    private val reference2 = City.of(
        """
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991
        """.trimIndent()
    )

    private val inputCity = City.of(AOC.getInput("/2023/day17.txt"))

    private val start = Path(to = Point(0, 0), direction = E)

    @Test
    fun `path traveled`() {
        start.walk(AHEAD) { 3 } shouldBe Path(
            to = Point(1, 0), direction = E,
            totalLoss = 3, straight = 1
        )

        start.walk(AHEAD) { null } shouldBe null

        val right = start.walk(RIGHT) { 1 }!!
        right shouldBe Path(
            to = Point(0, 1), direction = S,
            totalLoss = 1, straight = 1
        )

        val rightLeft = right.walk(LEFT) { 7 }!!

        rightLeft shouldBe Path(
            to = Point(1, 1), direction = E,
            totalLoss = 8, straight = 1
        )

        rightLeft.straight shouldBe 1
        rightLeft.walk(AHEAD) { 1 }?.straight shouldBe 2
    }

    @Test
    fun `parse city map`() {
        referenceCity.lossAt(0, 0) shouldBe 2
        referenceCity.lossAt(1, 0) shouldBe 4
        referenceCity.lossAt(-1, 0) shouldBe null
    }

    @Test
    fun `generate continuations`() {
        val constantLoss: LossFunction = { 1 }
        start.next(constantLoss).toList() shouldBe listOf(
            start.walk(AHEAD, constantLoss),
            start.walk(LEFT, constantLoss),
            start.walk(RIGHT, constantLoss)
        )

        start.walk(AHEAD, constantLoss)
            ?.walk(AHEAD, constantLoss)
            ?.walk(AHEAD, constantLoss)
            ?.next(constantLoss)
            ?.map { it.direction }
            ?.toList() shouldBe
                listOf(N, S)
    }

    @Test
    fun `traversal policy`() {
        val start = Path(to = Point(0, 0), direction = E, policy = Policy(4, 10))

        start.next { 3 }.toList().map { it.direction } shouldBe listOf(E)

        (1..5).fold(sequenceOf(start)) { p, c -> p.flatMap { it.next { c } } }
            .toList().map { it.direction } shouldBe listOf(E, N, S)
    }

    @Test
    fun `average heat loss in the city`() {
        referenceCity.averageLoss shouldBe 5
    }

    @Test
    fun `shortest path`() {
        referenceCity.findShortestPath() shouldBe 102
    }


    @Test
    fun `part 1`() {
        inputCity.findShortestPath() shouldBe 1039
    }

    @Test
    fun `shortest path with policy`() {
        referenceCity.findShortestPath(
            policy = Policy(4, 10)
        ) shouldBe 94

        reference2.findShortestPath(
            policy = Policy(4, 10)
        ) shouldBe 71
    }

    @Test
    fun `part 2`() {
        inputCity.findShortestPath(policy = Policy(4, 10)) shouldBe 1201
    }
}