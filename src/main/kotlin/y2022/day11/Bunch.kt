package y2022.day11

import util.lcm

data class Bunch(val monkeys: List<Monkey>) {
    val business = monkeys.map { it.inspectedItemCount }.sorted().takeLast(2).reduce(Long::times)

    fun round(): Bunch {
        return copy(monkeys = this.monkeys.indices.fold(this.monkeys) { monkeys, idx ->
            val monkey = monkeys[idx]
            val throws = monkey.turn()
            monkeys.map { it.perform(monkey.no, throws) }
        })
    }

    fun rounds(n: Int = 20) = (1..n).fold(this) { b, _ -> b.round() }

    fun worryMode() = copy(monkeys = monkeys.map { it.worryMode() })

    companion object {
        fun of(monkeys: List<Monkey>) = getBunchLcm(monkeys).let { lcm ->
            Bunch(monkeys.map {
                it.optimize(lcm)
            })
        }

        private fun getBunchLcm(monkeys: List<Monkey>) =
            monkeys.map { it.test }.reduce(::lcm)
    }
}
