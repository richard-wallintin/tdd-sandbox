package y2022.day11

import java.math.BigInteger

typealias Item = BigInteger
typealias Throw = Pair<Item, MonkeyNo>

data class Monkey(
    val no: MonkeyNo,
    val items: List<Item>,
    val operation: (Item) -> Item,
    val test: Item,
    val ifTrue: MonkeyNo,
    val ifFalse: MonkeyNo,
    val inspectedItemCount: Long = 0,
    val relief: BigInteger = BigInteger.valueOf(3),
    val optimization: BigInteger? = null
) {
    fun test(item: Item) = item % test == BigInteger.ZERO

    fun turn(): List<Throw> {
        return items.map {
            var afterInspection = (operation(it) / relief)
            if (optimization != null)
                afterInspection %= optimization
            afterInspection to (if (test(afterInspection)) ifTrue else ifFalse)
        }
    }

    fun perform(from: MonkeyNo, throws: List<Throw>): Monkey {
        return copy(
            items = remainingItems(items, from) + newItems(throws),
            inspectedItemCount = if (other(from)) inspectedItemCount else
                inspectedItemCount + items.size
        )
    }

    private fun newItems(throws: List<Pair<Item, MonkeyNo>>): List<Item> {
        return throws.filter { it.second == no }.map { it.first }
    }

    private fun remainingItems(
        old: List<Item>,
        from: MonkeyNo
    ): List<Item> {
        return if (other(from)) old
        else emptyList()
    }

    private fun other(from: MonkeyNo) = from != no

    fun worryMode() = copy(relief = BigInteger.ONE)
    fun optimize(lcm: BigInteger) = copy(optimization = lcm)
}
