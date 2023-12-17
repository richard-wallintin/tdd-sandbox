package y2023.day7

import AOC
import y2023.day7.Bet.Companion.totalWinnings
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.split
import y2023.day7.Bet
import y2023.day7.Hand
import y2023.day7.Label
import y2023.day7.Type

class CamelCardsTest {

    private val referenceInput = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()

    @Test
    fun `label ordering`() {
        Label.A shouldBeGreaterThan Label.K
        Label.N2 shouldBeLessThan Label.T
    }

    @Test
    fun `recognize Hands`() {
        Hand.of("AAAAA").type shouldBe Type.FiveOfAKind
        Hand.of("AA8AA").type shouldBe Type.FourOfAKind
        Hand.of("23332").type shouldBe Type.FullHouse
        Hand.of("TTT98").type shouldBe Type.ThreeOfAKind
        Hand.of("23432").type shouldBe Type.TwoPair
        Hand.of("A23A4").type shouldBe Type.OnePair
        Hand.of("23456").type shouldBe Type.HighCard
    }

    @Test
    fun `hand ordering`() {
        referenceInput.lineSequence().map { it.split().first() }.map { Hand.of(it) }.sorted()
            .toList() shouldBe listOf(
            Hand.of("32T3K"),
            Hand.of("KTJJT"),
            Hand.of("KK677"),
            Hand.of("T55J5"),
            Hand.of("QQQJA")
        )
    }

    @Test
    fun `bet parsing`() {
        Bet.of("32T3K 765") shouldBe Bet(Hand.of("32T3K"), 765)
    }

    @Test
    fun `total winnings`() {
        Bet.ofMany(referenceInput).totalWinnings() shouldBe 6440
    }

    private val aocInput = AOC.getInput("/2023/day7.txt")

    @Test
    fun `part 1`() {
        Bet.ofMany(aocInput).totalWinnings() shouldBe 252_656_917
    }

    @Test
    fun `label order with jokers`() {
        listOf(Label.A, Label.T, Label.J, Label.N3)
            .sortedWith(Hand.Companion::compareLabelsWithJoker) shouldBe
                listOf(Label.J, Label.N3, Label.T, Label.A)
    }

    @Test
    fun `hand types with joker`() {
        Hand.of("AAAAA", joker = true).type shouldBe Type.FiveOfAKind
        Hand.of("32T3K", joker = true).type shouldBe Type.OnePair
        Hand.of("T55J5", joker = true).type shouldBe Type.FourOfAKind
        Hand.of("KK677", joker = true).type shouldBe Type.TwoPair
        Hand.of("KTJJT", joker = true).type shouldBe Type.FourOfAKind
        Hand.of("QQQJA", joker = true).type shouldBe Type.FourOfAKind
        Hand.of("QQQJQ", joker = true).type shouldBe Type.FiveOfAKind
        Hand.of("QQJJQ", joker = true).type shouldBe Type.FiveOfAKind
        Hand.of("2345J", joker = true).type shouldBe Type.OnePair
        Hand.of("234JJ", joker = true).type shouldBe Type.ThreeOfAKind
        Hand.of("23JJJ", joker = true).type shouldBe Type.FourOfAKind
        Hand.of("2JJJJ", joker = true).type shouldBe Type.FiveOfAKind
        Hand.of("2234J", joker = true).type shouldBe Type.ThreeOfAKind
        Hand.of("22JJJ", joker = true).type shouldBe Type.FiveOfAKind
        Hand.of("JJJJJ", joker = true).type shouldBe Type.FiveOfAKind
        Hand.of("7QQJ7", joker = true).type shouldBe Type.FullHouse
    }

    @Test
    fun `total winnings reference with joker`() {
        Bet.ofMany(referenceInput, joker = true).totalWinnings() shouldBe 5_905
    }

    @Test
    fun `part 2`() {
        Bet.ofMany(aocInput, joker = true).totalWinnings() shouldBe 253_499_763
    }
}