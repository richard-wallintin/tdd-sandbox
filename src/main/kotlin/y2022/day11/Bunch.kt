package y2022.day11

data class Bunch(val monkeys: List<Monkey>) {
    val business = monkeys.map { it.inspectedItemCount }.sorted().takeLast(2).reduce(Int::times)

    fun round(): Bunch {
        return copy(monkeys = this.monkeys.indices.fold(this.monkeys) { monkeys, idx ->
            val monkey = monkeys[idx]
            val throws = monkey.turn()
            monkeys.map { it.perform(monkey.no, throws) }
        })
    }
}
