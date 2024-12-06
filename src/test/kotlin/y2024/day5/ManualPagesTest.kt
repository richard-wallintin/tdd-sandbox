package y2024.day5

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2024.day5.Rule.Companion.adjust
import y2024.day5.Rule.Companion.filter
import y2024.day5.Rule.Companion.mustBeBefore
import y2024.day5.Update.Companion.middlePageSum

class ManualPagesTest {

    @Test
    fun `parse rules`() {
        Rule.parse(
            """
            47|53
            97|13
            97|61
        """.trimIndent()
        ) shouldBe listOf(
            47 mustBeBefore 53,
            97 mustBeBefore 13,
            97 mustBeBefore 61
        )
    }

    @Test
    fun `parse updates`() {
        Update.parse(
            """
            1|2
            
            75,47,61,53,29
            97,61,53,29,13
            """.trimIndent()
        ) shouldBe listOf(
            Update(listOf(75, 47, 61, 53, 29)),
            Update(listOf(97, 61, 53, 29, 13))
        )
    }

    private val sampleInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()

    @Test
    fun `single rule checks updates`() {
        val rule = 97 mustBeBefore 75
        rule.isValid(Update(listOf(75, 97, 47, 61, 53))) shouldBe false
        rule.isValid(Update(listOf(75, 47, 61, 53, 29))) shouldBe true
        rule.isValid(Update(listOf(1, 2, 3))) shouldBe true
    }

    @Test
    fun `rules match updates`() {
        val rules = Rule.parse(sampleInput)
        val updates = Update.parse(sampleInput)

        updates.filter(rules) shouldBe updates.subList(0, 3)
    }

    @Test
    fun `middle page sum`() {
        Rule.readValidUpdates(sampleInput).middlePageSum() shouldBe 143
    }

    private val input = AOC.getInput("/2024/day5.txt")

    @Test
    fun part1() {
        Rule.readValidUpdates(input).middlePageSum() shouldBe 5713
    }

    @Test
    fun `use rules to sort`() {
        val sampleRules = Rule.parse(sampleInput)

        sampleRules.adjust(Update(listOf(75, 97, 47, 61, 53))) shouldBe Update(
            listOf(97, 75, 47, 61, 53)
        )

        sampleRules.adjust(Update(listOf(61, 13, 29))) shouldBe Update(
            listOf(61, 29, 13)
        )

        sampleRules.adjust(Update(listOf(97, 13, 75, 29, 47))) shouldBe Update(
            listOf(97, 75, 47, 29, 13)
        )
    }

    @Test
    fun `adjust batch`() {
        Rule.adjustInvalidUpdates(sampleInput).middlePageSum() shouldBe 123
    }

    @Test
    fun part2() {
        Rule.adjustInvalidUpdates(input).middlePageSum() shouldBe 5180
    }
}