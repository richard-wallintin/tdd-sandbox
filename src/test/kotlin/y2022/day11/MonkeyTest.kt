package y2022.day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.big

class MonkeyTest {

    /**
     * Monkey 0:
     *   Starting items: 79, 98
     *   Operation: new = old * 19
     *   Test: divisible by 23
     *     If true: throw to monkey 2
     *     If false: throw to monkey 3
     */
    private val monkey0 = Monkey(
        no = MonkeyNo(0),
        items = listOf(79, 98).big,
        operation = {  it * 19.big },
        test = 23.big,
        ifTrue = MonkeyNo(2),
        ifFalse = MonkeyNo(3)
    )

    @Test
    fun `a monkey's turn`() {
        monkey0.turn() shouldBe listOf(
            500.big to MonkeyNo(3),
            620.big to MonkeyNo(3)
        )
    }

    /**
     * Monkey 1:
     *   Starting items: 54, 65, 75, 74
     *   Operation: new = old + 6
     *   Test: divisible by 19
     *     If true: throw to monkey 2
     *     If false: throw to monkey 0
     */
    private val monkey1 = Monkey(
        no = MonkeyNo(1),
        items = listOf(54, 65, 75, 74).big,
        operation = {  it + 6.big },
        test = 19.big,
        ifTrue = MonkeyNo(2),
        ifFalse = MonkeyNo(0)
    )

    @Test
    fun `try monkey 1 and perform throws`() {
        val monkey1Throws = listOf(
            20.big to MonkeyNo(0),
            23.big to MonkeyNo(0),
            27.big to MonkeyNo(0),
            26.big to MonkeyNo(0)
        )
        monkey1.turn() shouldBe monkey1Throws
        val monkey1After = monkey1.perform(monkey1.no, monkey1Throws)
        monkey1After.items shouldBe emptyList()
        val monkey0After = monkey0.perform(monkey1.no, monkey1Throws)
        monkey0After.items shouldBe listOf(79, 98, 20, 23, 27, 26).big
    }

    /**
     * Monkey 2:
     *   Starting items: 79, 60, 97
     *   Operation: new = old * old
     *   Test: divisible by 13
     *     If true: throw to monkey 1
     *     If false: throw to monkey 3
     */
    private val monkey2 = Monkey(
        no = MonkeyNo(2),
        items = listOf(79, 60, 97).big,
        operation = { it * it },
        test = 13.big,
        ifTrue = MonkeyNo(1),
        ifFalse = MonkeyNo(3)
    )

    /**
     * Monkey 3:
     *   Starting items: 74
     *   Operation: new = old + 3
     *   Test: divisible by 17
     *     If true: throw to monkey 0
     *     If false: throw to monkey 1
     */
    private val monkey3 = Monkey(
        no = MonkeyNo(3),
        items = listOf(74).big,
        operation = { it + 3.big },
        test = 17.big,
        ifTrue = MonkeyNo(0),
        ifFalse = MonkeyNo(1)
    )

    private val sampleBunch = Bunch.of(listOf(monkey0, monkey1, monkey2, monkey3))

    @Test
    fun `compute full round for entire bunch`() {
        val round1 = sampleBunch.round()

        round1.monkeys[0].items shouldBe listOf(20, 23, 27, 26).big
        round1.monkeys[1].items shouldBe listOf(2080, 25, 167, 207, 401, 1046).big
        round1.monkeys[2].items shouldBe emptyList()
        round1.monkeys[3].items shouldBe emptyList()

        val round2 = round1.round()

        round2.monkeys[0].items shouldBe listOf(695, 10, 71, 135, 350).big
        round2.monkeys[1].items shouldBe listOf(43, 49, 58, 55, 362).big
        round2.monkeys[2].items shouldBe emptyList()
        round2.monkeys[3].items shouldBe emptyList()
    }

    @Test
    fun `20 rounds`() {
        val round20 = sampleBunch.rounds(20)

        round20.monkeys[0].items shouldBe listOf(10, 12, 14, 26, 34).big
        round20.monkeys[1].items shouldBe listOf(245, 93, 53, 199, 115).big
        round20.monkeys[2].items shouldBe emptyList()
        round20.monkeys[3].items shouldBe emptyList()

        round20.monkeys[0].inspectedItemCount shouldBe 101
        round20.monkeys[1].inspectedItemCount shouldBe 95
        round20.monkeys[2].inspectedItemCount shouldBe 7
        round20.monkeys[3].inspectedItemCount shouldBe 105

        round20.business shouldBe 10605
    }

    private val puzzleBunch = Bunch.of(
        listOf(
            Monkey(
                no = MonkeyNo(0),
                items = listOf(66, 59, 64, 51).big,
                operation = { it * 3.big },
                test = 2.big,
                ifTrue = MonkeyNo(1),
                ifFalse = MonkeyNo(4)
            ),

            Monkey(
                no = MonkeyNo(1),
                items = listOf(67, 61).big,
                operation = { it * 19.big },
                test = 7.big,
                ifTrue = MonkeyNo(3),
                ifFalse = MonkeyNo(5)
            ),

            Monkey(
                no = MonkeyNo(2),
                items = listOf(86, 93, 80, 70, 71, 81, 56).big,
                operation = { it + 2.big },
                test = 11.big,
                ifTrue = MonkeyNo(4),
                ifFalse = MonkeyNo(0)
            ),

            Monkey(
                no = MonkeyNo(3),
                items = listOf(94).big,
                operation = { it * it },
                test = 19.big,
                ifTrue = MonkeyNo(7),
                ifFalse = MonkeyNo(6)
            ),

            Monkey(
                no = MonkeyNo(4),
                items = listOf(71, 92, 64).big,
                operation = { it + 8.big },
                test = 3.big,
                ifTrue = MonkeyNo(5),
                ifFalse = MonkeyNo(1)
            ),

            Monkey(
                no = MonkeyNo(5),
                items = listOf(58, 81, 92, 75, 56).big,
                operation = { it + 6.big },
                test = 5.big,
                ifTrue = MonkeyNo(3),
                ifFalse = MonkeyNo(6)
            ),

            Monkey(
                no = MonkeyNo(6),
                items = listOf(82, 98, 77, 94, 86, 81).big,
                operation = { it + 7.big },
                test = 17.big,
                ifTrue = MonkeyNo(7),
                ifFalse = MonkeyNo(2)
            ),

            Monkey(
                no = MonkeyNo(7),
                items = listOf(54, 95, 70, 93, 88, 93, 63, 50).big,
                operation = { it + 4.big },
                test = 13.big,
                ifTrue = MonkeyNo(2),
                ifFalse = MonkeyNo(0)
            )
        )
    )

    @Test
    fun part1() {
        val round20 = puzzleBunch.rounds(20)

        round20.business shouldBe 90294
    }

    @Test
    fun `worry mode for sample bunch`() {
        val bunch = sampleBunch.worryMode()
        bunch.rounds(20).business shouldBe (103*99)
        bunch.rounds(1000).business shouldBe (5192L*5204L)
    }

    @Test
    fun `part 2`() {
        puzzleBunch.worryMode().rounds(10_000).business shouldBe 18170818354L
    }
}