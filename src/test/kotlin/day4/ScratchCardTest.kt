package day4

import day4.ScratchCard.Companion.pileWorth
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

fun Int.pow(exp: Int): Int = if (exp == 0) 1 else (this * this.pow(exp - 1))

data class ScratchCard(val no: Int, val winning: Set<Int>, val select: Set<Int>) {
    val winningNumbers = select.count { it in winning }
    val worth = if (winningNumbers > 0) 2.pow(winningNumbers - 1) else 0

    companion object {
        fun of(s: String): ScratchCard =
            Regex("Card\\s+(\\d+):\\s+((?:\\d+\\s+)+)\\|((?:\\s+\\d+)+\\s*)").matchEntire(s)?.destructured?.let { (no, winning, select) ->
                ScratchCard(
                    no = no.toInt(),
                    winning = winning.numbers(),
                    select = select.numbers(),
                )
            } ?: throw IllegalArgumentException("cant parse $s")


        private fun String.numbers() =
            split(Regex("\\s+")).filter { it.isNotBlank() }.map { it.toInt() }.toSet()

        fun String.pileWorth() = lineSequence().map { ScratchCard.of(it).worth }.sum()
    }

}

class ScratchCardTest {

    private val card1 = ScratchCard(
        no = 1,
        winning = setOf(41, 48, 83, 86, 17),
        select = setOf(83, 86, 6, 31, 17, 9, 48, 53)
    )

    private val refData = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()

    @Test
    fun `scratch card parsing`() {
        ScratchCard.of("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53") shouldBe card1
    }

    @Test
    fun worth() {
        card1.worth shouldBe 8
    }

    @Test
    fun `pile worth`() {
        refData.pileWorth() shouldBe 13
    }

    @Test
    fun `part 1`() {
        AOC.getInput("/day4.txt").pileWorth() shouldBe 22_897
    }
}