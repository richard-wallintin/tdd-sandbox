package y2023.day7

import util.split

data class Bet(val hand: Hand, val bid: Int) : Comparable<Bet> {
    companion object {
        fun of(s: String, joker: Boolean = false) = s.split().let { (h, b) -> Bet(Hand.of(h, joker), b.toInt()) }
        fun ofMany(text: String, joker: Boolean = false): Sequence<Bet> {
            return text.lineSequence().map { of(it, joker) }
        }

        fun Sequence<Bet>.totalWinnings() =
            sorted().withIndex().map { (it.index + 1) * it.value.bid }.sum()
    }

    override fun compareTo(other: Bet) = hand.compareTo(other.hand)
}
