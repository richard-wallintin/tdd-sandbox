package y2024.day22

import util.forever

typealias Seq = List<Int>

@JvmInline
value class SecretNumber(val value: UInt) {
    val price: Int get() = (value % 10u).toInt()

    fun next(): SecretNumber {
        var s = value
        s = prune((s shl 6) xor s)
        s = prune((s shr 5) xor s)
        s = prune((s shl 11) xor s)

        return SecretNumber(s)
    }

    fun start() = forever { next() }

    override fun toString() = value.toString(10)
    fun next(n: Int) = start().elementAt(n)

    fun sellAt(seq: List<Int>) =
        delta4().firstOrNull { it.matches(seq) }?.price ?: 0


    private fun delta4() = start().take(2000).map { it.price }
        .zipWithNext { old, price -> price to (price - old) }
        .zipWithNext { prev, cur -> PriceSpot(cur.first, listOf(prev.second, cur.second)) }
        .zipWithNext(PriceSpot::extend)
        .zipWithNext(PriceSpot::extend)


    companion object {
        private const val B24: UInt = 0xFFFFFFu

        fun prune(value: UInt) = value and B24

        fun of(intValue: Int) = SecretNumber(intValue.toUInt())

        fun parseMany(text: String) = text.lineSequence().map { SecretNumber(it.toUInt()) }
        fun Sequence<SecretNumber>.sum() = sumOf { it.value.toULong() }

        fun List<SecretNumber>.buildIndex(): Map<Seq, Int> {
            data class Entry(var lastBuyer: Int = -1, var bananas: Int = 0)

            val idx = mutableMapOf<Seq, Entry>()

            this.forEachIndexed { buyerNo, secret ->
                secret.delta4().forEach { spot ->
                    val e = idx.getOrPut(spot.seq, ::Entry)
                    if (e.lastBuyer < buyerNo) {
                        e.lastBuyer = buyerNo
                        e.bananas += spot.price
                    }
                }
            }

            return idx.mapValues { (_, v) -> v.bananas }
        }

        fun Map<Seq, Int>.mostBananas() = values.max()
    }

    data class PriceSpot(val price: Int, val seq: Seq) {
        private val length get() = seq.size

        private fun combine(prev: PriceSpot) = copy(seq = prev.seq + seq.drop(length - 1))
        fun extend(next: PriceSpot) = next.combine(this)

        fun matches(seq: Seq) = this.seq == seq
    }
}