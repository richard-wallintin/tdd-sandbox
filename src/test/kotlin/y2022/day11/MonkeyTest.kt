package y2022.day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

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
        items = listOf(79, 98),
        operation = { it: Int -> it * 19 },
        test = { it: Int -> it % 23 == 0 },
        ifTrue = MonkeyNo(2),
        ifFalse = MonkeyNo(3)
    )

    @Test
    fun `a monkey's turn`() {
        monkey0.turn() shouldBe listOf(
            500 to MonkeyNo(3),
            620 to MonkeyNo(3)
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
        items = listOf(54, 65, 75, 74),
        operation = { it: Int -> it + 6 },
        test = { it: Int -> it % 19 == 0 },
        ifTrue = MonkeyNo(2),
        ifFalse = MonkeyNo(0)
    )

    @Test
    fun `try monkey 1 and perform throws`() {
        val monkey1Throws = listOf(
            20 to MonkeyNo(0),
            23 to MonkeyNo(0),
            27 to MonkeyNo(0),
            26 to MonkeyNo(0)
        )
        monkey1.turn() shouldBe monkey1Throws
        val monkey1After = monkey1.perform(monkey1.no, monkey1Throws)
        monkey1After.items shouldBe emptyList()
        val monkey0After = monkey0.perform(monkey1.no, monkey1Throws)
        monkey0After.items shouldBe listOf(79, 98, 20, 23, 27, 26)
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
        items = listOf(79, 60, 97),
        operation = { it * it },
        test = { it % 13 == 0 },
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
        items = listOf(74),
        operation = { it + 3 },
        test = { it % 17 == 0 },
        ifTrue = MonkeyNo(0),
        ifFalse = MonkeyNo(1)
    )

    private val sampleBunch = Bunch(listOf(monkey0, monkey1, monkey2, monkey3))

    @Test
    fun `compute full round for entire bunch`() {
        val round1 = sampleBunch.round()

        round1.monkeys[0].items shouldBe listOf(20, 23, 27, 26)
        round1.monkeys[1].items shouldBe listOf(2080, 25, 167, 207, 401, 1046)
        round1.monkeys[2].items shouldBe emptyList()
        round1.monkeys[3].items shouldBe emptyList()

        val round2 = round1.round()

        round2.monkeys[0].items shouldBe listOf(695, 10, 71, 135, 350)
        round2.monkeys[1].items shouldBe listOf(43, 49, 58, 55, 362)
        round2.monkeys[2].items shouldBe emptyList()
        round2.monkeys[3].items shouldBe emptyList()
    }

    @Test
    fun `20 rounds`() {
        val round20 = (1..20).fold(sampleBunch) { b, _ -> b.round() }

        round20.monkeys[0].items shouldBe listOf(10, 12, 14, 26, 34)
        round20.monkeys[1].items shouldBe listOf(245, 93, 53, 199, 115)
        round20.monkeys[2].items shouldBe emptyList()
        round20.monkeys[3].items shouldBe emptyList()

        round20.monkeys[0].inspectedItemCount shouldBe 101
        round20.monkeys[1].inspectedItemCount shouldBe 95
        round20.monkeys[2].inspectedItemCount shouldBe 7
        round20.monkeys[3].inspectedItemCount shouldBe 105

        round20.business shouldBe 10605
    }

    @Test
    fun part1() {
        val bunch = Bunch(
            listOf(
                Monkey(
                    no = MonkeyNo(0),
                    items = listOf(66, 59, 64, 51),
                    operation = { it * 3 },
                    test = { it % 2 == 0 },
                    ifTrue = MonkeyNo(1),
                    ifFalse = MonkeyNo(4)
                ),

                Monkey(
                    no = MonkeyNo(1),
                    items = listOf(67, 61),
                    operation = { it * 19 },
                    test = { it % 7 == 0 },
                    ifTrue = MonkeyNo(3),
                    ifFalse = MonkeyNo(5)
                ),

                Monkey(
                    no = MonkeyNo(2),
                    items = listOf(86, 93, 80, 70, 71, 81, 56),
                    operation = { it + 2 },
                    test = { it % 11 == 0 },
                    ifTrue = MonkeyNo(4),
                    ifFalse = MonkeyNo(0)
                ),

                Monkey(
                    no = MonkeyNo(3),
                    items = listOf(94),
                    operation = { it * it },
                    test = { it % 19 == 0 },
                    ifTrue = MonkeyNo(7),
                    ifFalse = MonkeyNo(6)
                ),

                Monkey(
                    no = MonkeyNo(4),
                    items = listOf(71, 92, 64),
                    operation = { it + 8 },
                    test = { it % 3 == 0 },
                    ifTrue = MonkeyNo(5),
                    ifFalse = MonkeyNo(1)
                ),

                Monkey(
                    no = MonkeyNo(5),
                    items = listOf(58, 81, 92, 75, 56),
                    operation = { it + 6 },
                    test = { it % 5 == 0 },
                    ifTrue = MonkeyNo(3),
                    ifFalse = MonkeyNo(6)
                ),

                Monkey(
                    no = MonkeyNo(6),
                    items = listOf(82, 98, 77, 94, 86, 81),
                    operation = { it + 7 },
                    test = { it % 17 == 0 },
                    ifTrue = MonkeyNo(7),
                    ifFalse = MonkeyNo(2)
                ),

                Monkey(
                    no = MonkeyNo(7),
                    items = listOf(54, 95, 70, 93, 88, 93, 63, 50),
                    operation = { it + 4 },
                    test = { it % 13 == 0 },
                    ifTrue = MonkeyNo(2),
                    ifFalse = MonkeyNo(0)
                )
            )
        )

        val round20 = (1..20).fold(bunch) { b, _ -> b.round() }

        round20.business shouldBe 90294
    }
}