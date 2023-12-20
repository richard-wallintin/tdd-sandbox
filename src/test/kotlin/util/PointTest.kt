package util

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PointTest {

    @Test
    fun `step distance`() {
        (Point(0,0) distance Point(1,1)) shouldBe 2
        (Point(1,0) distance Point(1,0)) shouldBe 0
        (Point(0,0) distance Point(10,8)) shouldBe 18
        (Point(10,10) distance Point(3,2)) shouldBe 15
    }
}