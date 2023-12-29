package util

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RectangleTest {
    @Test
    fun `overlap computation`() {
        val r1 = Rectangle(Point(0, 0), Point(3, 3))

        r1.overlap(Rectangle(Point(0, 0), Point(1, 1))) shouldBe
                Rectangle(Point(0, 0), Point(1, 1))

        r1.overlap(Rectangle(Point(-10, -10), Point(1, 1))) shouldBe
                Rectangle(Point(0, 0), Point(1, 1))

        r1.overlap(Rectangle(Point(4, 4), Point(5, 7))) shouldBe
                null

        r1.overlap(Rectangle(Point(3, 3), Point(5, 7))) shouldBe
                Rectangle(Point(3, 3), Point(3, 3))
    }

    @Test
    fun `auto-adjust points`() {
        Rectangle.of(Point(0, 1), Point(1, 0)) shouldBe
                Rectangle(Point(0, 0), Point(1, 1))
    }

    @Test
    fun `tiled rectangles`() {
        Rectangle.ofTiles(Point(0, 0), Point(0, 0)) shouldBe
                Rectangle(Point(0, 0), Point(1, 1))
    }

    @Test
    fun area() {
        Rectangle(Point(3, 3), Point(3, 3)).area shouldBe 0
    }
}