package y2023.day15

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2023.day15.Boxes.Box
import y2023.day15.Boxes.Lens
import y2023.day15.Step.Companion.applyTo
import y2023.day15.Step.Companion.hash
import y2023.day15.Step.Companion.verificationNumber

class HashingTest {

    @Test
    fun `hashing function`() {
        hash("H") shouldBe 200
        hash("HA") shouldBe 153
        hash("HAS") shouldBe 172
        hash("HASH") shouldBe 52
    }

    private val referenceProcuedure = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"

    @Test
    fun `reference procedure hash`() {
        verificationNumber(referenceProcuedure) shouldBe 1320
    }

    private val inputProcedure = AOC.getInput("/2023/day15.txt")

    @Test
    fun `part 1`() {
        verificationNumber(inputProcedure) shouldBe 501680
    }

    @Test
    fun `parse step`() {
        val step = Step.of("rn=1")
        step.label shouldBe "rn"
        step.boxNumber shouldBe 0
        step.focalLength shouldBe 1

        val step2 = Step.of("cm-")
        step2.label shouldBe "cm"
        step2.boxNumber shouldBe 0
    }

    @Test
    fun `apply steps`() {
        val step1 = Step.of("rn=1")
        val step2 = Step.of("cm-")

        var boxes = Boxes().let(step1::applyTo)
        boxes[0] shouldBe Box(listOf(Lens("rn", 1)))

        boxes = boxes.let(step2::applyTo)
        boxes[0] shouldBe Box(listOf(Lens("rn", 1)))
    }

    @Test
    fun `reference procedure`() {
        val boxes = Step.ofMany(referenceProcuedure).applyTo()

        boxes[0] shouldBe Box(listOf(Lens("rn", 1), Lens("cm", 2)))
        boxes[3] shouldBe Box(listOf(Lens("ot", 7), Lens("ab", 5), Lens("pc", 6)))

        boxes.focusingPower() shouldBe 145
    }

    @Test
    fun `part 2`() {
        Step.ofMany(inputProcedure).applyTo().focusingPower() shouldBe 241094
    }
}