package y2023.day4

import util.integers
import util.power

data class ScratchCard(val no: Int, val winning: Set<Int>, val select: Set<Int>) {
    val winCount = select.count { it in winning }
    val worth = if (winCount > 0) 2.power(winCount - 1) else 0

    companion object {
        fun of(s: String): ScratchCard =
            Regex("Card\\s+(\\d+):\\s+((?:\\d+\\s+)+)\\|((?:\\s+\\d+)+\\s*)").matchEntire(s)?.destructured?.let { (no, winning, select) ->
                ScratchCard(
                    no = no.toInt(),
                    winning = winning.integers().toSet(),
                    select = select.integers().toSet(),
                )
            } ?: throw IllegalArgumentException("cant parse $s")


        fun String.pileWorth() = lineSequence().map { of(it).worth }.sum()
    }

}

data class Pile(val cardCounts: Map<Int, Int> = mapOf()) {

    val totalCards = cardCounts.values.sum()

    companion object {
        fun of(data: String): Pile {
            val cardCounts: MutableMap<Int, Int> = mutableMapOf()

            val lastCard = data.lineSequence().map { ScratchCard.of(it) }.onEach { card ->
                cardCounts.putIfAbsent(card.no, 1)
                val multiplier = cardCounts[card.no] ?: 1

                if (card.winCount > 0) {
                    (1..card.winCount).forEach {
                        cardCounts.compute(card.no + it) { _, count ->
                            if (count == null) (1 + multiplier) else (count + multiplier)
                        }
                    }
                }
            }.last()

            cardCounts.keys.removeIf { it > lastCard.no }

            return Pile(cardCounts.toMap())
        }
    }

}