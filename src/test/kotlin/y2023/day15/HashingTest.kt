package y2023.day15

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class HashingTest {

    @Test
    fun `hashing function`() {
        hash("H") shouldBe 200
        hash("HA") shouldBe 153
        hash("HAS") shouldBe 172
        hash("HASH") shouldBe 52
    }

    private fun hash(s: String): Int {
        return s.fold(0) { v, c -> v.plus(c.code).times(17).mod(256) }
    }

    @Test
    fun `reference procedure hash`() {
        verificationNumber("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7") shouldBe 1320
    }

    @Test
    fun `part 1`() {
        verificationNumber(AOC.getInput("/2023/day15.txt")) shouldBe 501680
    }

    private fun verificationNumber(procedure: String) = procedure.split(Regex(",")).sumOf {
        hash(it)
    }
}