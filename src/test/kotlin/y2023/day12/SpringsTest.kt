package y2023.day12

import AOC
import y2023.day12.ConditionRecord.Companion.totalResolutions
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2023.day12.ConditionRecord
import y2023.day12.DamagedGroups
import y2023.day12.SpringRow
import y2023.day12.SpringState

class SpringsTest {

    private val reference = """
                ???.### 1,1,3
                .??..??...?##. 1,1,3
                ?#?#?#?#?#?#?#? 1,3,1,6
                ????.#...#... 4,1,1
                ????.######..#####. 1,6,5
                ?###???????? 3,2,1
            """.trimIndent()

    private val input = AOC.getInput("/2023/day12.txt")

    private fun row(s: String) = SpringRow.parse(s)
    private fun groups(vararg g: Int) = DamagedGroups(g.toList())

    @Test
    fun `parsing and representation`() {
        SpringRow.parse("#.#.###") shouldBe SpringRow(
            listOf(
                SpringState.DAMAGED,
                SpringState.OPERATIONAL,
                SpringState.DAMAGED,
                SpringState.OPERATIONAL,
                SpringState.DAMAGED,
                SpringState.DAMAGED,
                SpringState.DAMAGED,
            )
        )

        SpringRow.parse("???.###") shouldBe SpringRow(
            listOf(
                SpringState.UNKNOWN,
                SpringState.UNKNOWN,
                SpringState.UNKNOWN,
                SpringState.OPERATIONAL,
                SpringState.DAMAGED,
                SpringState.DAMAGED,
                SpringState.DAMAGED,
            )
        )
    }

    @Test
    fun `parsing group stats`() {
        DamagedGroups.parse("1,1,3") shouldBe DamagedGroups(listOf(1, 1, 3))
    }

    @Test
    fun `complete records can compute groups`() {
        SpringRow.parse("#").groups() shouldBe DamagedGroups(listOf(1))
        SpringRow.parse("##").groups() shouldBe DamagedGroups(listOf(2))
        SpringRow.parse("##.#").groups() shouldBe DamagedGroups(listOf(2, 1))
        SpringRow.parse("#.#.###").groups() shouldBe DamagedGroups(listOf(1, 1, 3))
    }

    @Test
    fun `resolve incomplete groups`() {
        SpringRow.parse("#").resolve().toList() shouldBe listOf(SpringRow.parse("#"))
        row("?").resolve().toList() shouldBe listOf(
            SpringRow.parse("#"),
            SpringRow.parse(".")
        )
        SpringRow.parse("??").resolve().toList() shouldBe listOf(
            SpringRow.parse("##"),
            SpringRow.parse(".#"),
            SpringRow.parse("#."),
            SpringRow.parse("..")
        )
    }

    @Test
    fun `count all matches`() {
        ConditionRecord.parse("???.### 1,1,3").countResolutions() shouldBe 1
    }

    @Test
    fun reference() {
        reference.totalResolutions() shouldBe 21
    }

    @Test
    fun `part 1`() {
        input.totalResolutions() shouldBe 7460
    }

    @Test
    fun unfold() {
        ConditionRecord.parse(".# 1")
            .unfold() shouldBe ConditionRecord.parse(".#?.#?.#?.#?.# 1,1,1,1,1")
    }

    @Test
    fun `unfold reference resolution`() {
        reference.totalResolutions(unfold = true) shouldBe 525152
    }

    @Test
    fun `resolve towards`() {
        row("?").resolveTo(DamagedGroups(listOf(1))).toList() shouldBe listOf(
            SpringRow.parse("#")
        )

        SpringRow.parse("??").resolveTo(DamagedGroups(listOf(1))).toList() shouldBe listOf(
            SpringRow.parse("#."),
            SpringRow.parse(".#")
        )
    }

    @Test
    fun `partial groups only using prefix`() {
        SpringRow.parse("#.#???").groups() shouldBe DamagedGroups(listOf(1, 1))
        SpringRow.parse("#.#???#").groups() shouldBe DamagedGroups(listOf(1, 1))
    }

    @Test
    fun `groups compatibility check head`() {
        (DamagedGroups(listOf(1, 1)) in DamagedGroups(listOf(1, 2))) shouldBe true
        (DamagedGroups(listOf(1, 2)) in DamagedGroups(listOf(1, 2))) shouldBe true
        (DamagedGroups(listOf(1, 2)) in DamagedGroups(listOf(1, 2, 3, 5, 6))) shouldBe true
        (DamagedGroups(listOf(1, 3)) in DamagedGroups(listOf(1, 2, 3, 5, 6))) shouldBe false
        (DamagedGroups(listOf(1, 2, 3)) in DamagedGroups(listOf(1, 2, 3, 5, 6))) shouldBe true
        (DamagedGroups(listOf(1, 1, 3)) in DamagedGroups(listOf(1, 2, 3, 5, 6))) shouldBe false
    }

    @Test
    fun `part 2`() {
        input.totalResolutions(unfold = true) shouldBe 6720660274964
    }

    @Test
    fun `pure counting algorithm`() {
        row(".").resolveCount(groups()) shouldBe 1

        row(".").resolveCount(groups(1)) shouldBe 0
        row("?").resolveCount(groups(2)) shouldBe 0
        row("?#?").resolveCount(groups(4)) shouldBe 0

        row("#").resolveCount(groups(1)) shouldBe 1
        row("").resolveCount(groups()) shouldBe 1

        row("?").resolveCount(groups(1)) shouldBe 1
        row("??").resolveCount(groups(1)) shouldBe 2
        row("???").resolveCount(groups(1)) shouldBe 3
        row("???").resolveCount(groups(1)) shouldBe 3

        row("???").resolveCount(groups(2)) shouldBe 2
        row("??").resolveCount(groups(3)) shouldBe 0

        row("???").resolveCount(groups(1, 2)) shouldBe 0

        row("#??").resolveCount(groups(1, 2)) shouldBe 0
        row("#??").resolveCount(groups(1)) shouldBe 1
        row("#??").resolveCount(groups(2)) shouldBe 1
        row("#???").resolveCount(groups(2, 1)) shouldBe 1
        row("#???").resolveCount(groups(2, 2)) shouldBe 0

        row("#.??").resolveCount(groups(2)) shouldBe 0
        row("#.??").resolveCount(groups(1)) shouldBe 1
        row("#.??").resolveCount(groups(1, 1)) shouldBe 2

        row("..??").resolveCount(groups(2)) shouldBe 1
        row("..??").resolveCount(groups(1)) shouldBe 2
        row("??").resolveCount(groups(1)) shouldBe 2

        row("??.").resolveCount(groups(1)) shouldBe 2
        row("??.").resolveCount(groups(2)) shouldBe 1
        row("??.").resolveCount(groups(3)) shouldBe 0
        row("??#").resolveCount(groups(3)) shouldBe 1

        row("?#?").resolveCount(groups(2)) shouldBe 2
        row("?#??").resolveCount(groups(3)) shouldBe 2
    }

    @Test
    fun `timing ref 1`() {
        ConditionRecord.parse("#.????#?????????#??# 1,2,11,2").unfold()
            .countResolutions() shouldBe 32
    }

    @Test
    fun `timing ref 2`() {
        ConditionRecord.parse("????.?????????? 1,1,2,2").unfold()
            .countResolutions() shouldBe 6214236069570L
    }
}