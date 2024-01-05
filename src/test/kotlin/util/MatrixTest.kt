package util

import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Matrix.Companion.column
import util.Matrix.Companion.matrix
import util.Matrix.Companion.nullMatrix
import util.MutableMatrix.Companion.mutableMatrix
import java.math.BigDecimal
import java.math.BigDecimal.valueOf

class MatrixTest {

    @Test
    fun `create empty matrix and manipulate it`() {
        val matrix = MutableMatrix(1 by 1)

        matrix[0, 0] shouldBe BigDecimal.ZERO
        matrix[0, 0] = BigDecimal.TEN
        matrix[0, 0] shouldBe BigDecimal.TEN

        val b = matrix.copy()
        b[0, 0] shouldBe BigDecimal.TEN
        matrix[0, 0] = BigDecimal.ONE
        b[0, 0] shouldBe BigDecimal.TEN
    }

    @Test
    fun `convenient matrix initialization and Number support`() {
        val m = (3 by 3).mutableMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6),
                listOf(7, 8, 9.5)
            )
        )
        m[1, 1] shouldBe valueOf(5)
        m[2, 2] shouldBe valueOf(9.5)

        m[0, 0] = 17
        m[0, 0] shouldBe valueOf(17)
    }

    @Test
    fun `determinant calculation`() {
        matrix(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9.5)
        ).determinant() shouldBe valueOf(-1.5)

        matrix(
            listOf(3, 8),
            listOf(4, 6)
        ).determinant() shouldBe valueOf(-14)

        matrix(
            listOf(6, 1, 1),
            listOf(4, -2, 5),
            listOf(2, 8, 7)
        ).determinant() shouldBe valueOf(-306)

        matrix(
            listOf(1.5, 0.5),
            listOf(0.3, 0.9),
        ).determinant() shouldBeEqualComparingTo valueOf(1.2)
    }

    @Test
    fun `inverse matrix`() {
        matrix(
            listOf(1, 0, 5),
            listOf(2, 1, 6),
            listOf(3, 4, 0)
        ).inverse() shouldBe matrix(
            listOf(-24, 20, -5),
            listOf(18, -15, 4),
            listOf(5, -4, 1)
        )
    }

    @Test
    fun `to string`() {
        matrix(listOf(1)).toString() shouldBe "[ 1 ]"
        matrix(listOf(1, 2)).toString() shouldBe "[ 1, 2 ]"
        matrix(listOf(1, 2), listOf(3, 4)).toString() shouldBe "[ 1, 2,\n 3, 4 ]"
        matrix(
            listOf(1, 2, 3),
            listOf(3, 4, 5),
            listOf(7, 8, 9)
        ).toString() shouldBe "[ 1, 2, 3,\n 3, 4, 5,\n 7, 8, 9 ]"

        (3 by 3).nullMatrix().toString() shouldBe "[ 0, 0, 0,\n 0, 0, 0,\n 0, 0, 0 ]"
        (2 by 3).nullMatrix().toString() shouldBe "[ 0, 0, 0,\n 0, 0, 0 ]"
        (3 by 2).nullMatrix().toString() shouldBe "[ 0, 0,\n 0, 0,\n 0, 0 ]"
    }

    @Test
    fun `cross product`() {
        matrix(
            listOf(3, 2, 1),
            listOf(1, 0, 2)
        ) x matrix(
            listOf(1, 2),
            listOf(0, 1),
            listOf(4, 0),
        ) shouldBe matrix(
            listOf(7, 8),
            listOf(9, 2),
        )
    }

    @Test
    fun `solve equation`() {
        matrix(listOf(3, 4), listOf(7, -2)).solve(column(listOf(18, 8)))
            .round() shouldBe column(listOf(2, 3))

        (matrix(
            listOf(2, 1, -1),
            listOf(-3, -1, 2),
            listOf(-2, 1, 2),
        ).solve(column(listOf(8, -11, -3))))
            .round() shouldBe column(listOf(2, 3, -1))
    }
}