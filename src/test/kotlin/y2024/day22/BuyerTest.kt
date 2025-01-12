package y2024.day22

import AOC
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2024.day22.SecretNumber.Companion.buildIndex
import y2024.day22.SecretNumber.Companion.mostBananas
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

    private val inputMarket = SecretNumber.parseMany(AOC.getInput("/2024/day22.txt"))

    @Test
    fun part1() {
        inputMarket.map { it.next(2000) }.sum() shouldBe 15006633487UL
    }


    @Test
    fun `compute prices`() {
        SecretNumber.of(123).start().take(10).map { it.price }.toList() shouldBe listOf(
            3, 0, 6, 5, 4, 4, 6, 4, 4, 2
        )
    }

    @Test
    fun `sell at sequence`() {
        SecretNumber.of(123).sellAt(listOf(-1, -1, 0, 2)) shouldBe 6
    }

    @Test
    fun `multiple sales`() {
        listOf(1, 2, 3, 2024).map { SecretNumber.of(it) }.sumOf {
            it.sellAt(listOf(-2, 1, -1, 3))
        } shouldBe 23
    }

    @Test
    fun `build buyer index`() {
        val buyerIndex = listOf(1, 2, 3, 2024).map { SecretNumber.of(it) }.buildIndex()
        buyerIndex shouldContain (listOf(-2, 1, -1, 3) to 23)
        buyerIndex.mostBananas() shouldBe 23
    }

    @Test
    fun part2() {
        inputMarket.toList().buildIndex().mostBananas() shouldBe 1710
    }
}
