package y2022.day11

typealias Item = Int
typealias Throw = Pair<Item, MonkeyNo>

data class Monkey(
    val no: MonkeyNo,
    val items: List<Item>,
    val operation: (Item) -> Item,
    val test: (Item) -> Boolean,
    val ifTrue: MonkeyNo,
    val ifFalse: MonkeyNo,
    val inspectedItemCount: Int = 0
) {
    fun turn(): List<Throw> {
        return items.map {
            val afterInspection: Int = operation(it) / 3
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
}
