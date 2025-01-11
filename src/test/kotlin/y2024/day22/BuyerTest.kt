package y2024.day22

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.forever
import y2024.day22.SecretNumber.Companion.sum

class BuyerTest {

    @Test
    fun `compute next secret number`() {
        SecretNumber.prune(100000000u) shouldBe 16113920u
        SecretNumber.of(123).next() shouldBe SecretNumber.of(15887950)

        SecretNumber.of(123).start().drop(1).take(10).map { it.toString() }.toList() shouldBe
                """
                    15887950
                    16495136
                    527345
                    704524
                    1553684
                    12683156
                    11100544
                    12249484
                    7753432
                    5908254
                """.trimIndent().lines()
    }

    @Test
    fun `predict 2000th secret number`() {
        SecretNumber.parseMany(
            """
            1
            10
            100
            2024
        """.trimIndent()
        ).map {
            it.next(2000).value
        }.toList() shouldBe listOf(
            8685429u,
            4700978u,
            15273692u,
            8667524u
        )
    }

    @Test
    fun part1() {
        SecretNumber.parseMany(AOC.getInput("/2024/day22.txt")).map {
            it.next(2000)
        }.sum() shouldBe 15006633487UL
    }


}

@JvmInline
value class SecretNumber(val value: UInt) {
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

    companion object {
        private const val B24: UInt = 0xFFFFFFu

        fun prune(value: UInt) = value and B24

        fun of(intValue: Int) = SecretNumber(intValue.toUInt())

        fun parseMany(text: String) = text.lineSequence().map { SecretNumber(it.toUInt()) }
        fun Sequence<SecretNumber>.sum() = sumOf { it.value.toULong() }
    }

}
