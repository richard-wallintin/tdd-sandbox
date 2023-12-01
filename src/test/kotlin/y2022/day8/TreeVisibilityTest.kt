package y2022.day8

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TreeVisibilityTest {

    @Test
    fun `parse forest`() {
        Forest.of("1") shouldBe Forest(listOf(listOf(1)))
        Forest.of("123\n456\n789") shouldBe Forest(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6),
                listOf(7, 8, 9)
            )
        )
    }

    @Test
    fun edgeTreesAreVisible() {
        Forest.of("1").visibleTreeCount shouldBe 1
        Forest.of("999\n959\n999").visibleTreeCount shouldBe 8
    }

    @Test
    fun middleTreeVisibility() {
        Forest.of("999\n959\n999").isVisible(1, 1) shouldBe false
        Forest.of("999\n954\n999").isVisible(1, 1) shouldBe true
        Forest.of("999\n459\n999").isVisible(1, 1) shouldBe true
        Forest.of("919\n959\n999").isVisible(1, 1) shouldBe true
        Forest.of("999\n959\n909").isVisible(1, 1) shouldBe true
        Forest.of("999\n555\n999").isVisible(1, 1) shouldBe false

        Forest.of("919\n959\n999").visibleTreeCount shouldBe 9
    }

    private val reference = Forest.of(
        """
                30373
                25512
                65332
                33549
                35390
            """.trimIndent()
    )

    @Test
    fun `part 1 reference`() {
        reference.visibleTreeCount shouldBe 21
    }

    @Test
    fun `part 1`() {
        inputForest().visibleTreeCount shouldBe 1825
    }

    private fun inputForest() = Forest.of(AOC.getInput("/2022/day8.txt"))

    @Test
    fun `scenic score examples`() {
        reference.scenicScore(1, 2) shouldBe 4
        reference.scenicScore(3, 2) shouldBe 8
        reference.maxScenicScore shouldBe 8
    }

    @Test
    fun `part two`() {
        inputForest().maxScenicScore shouldBe 235_200
    }
}