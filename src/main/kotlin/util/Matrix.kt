package util

import util.MutableMatrix.Companion.mutableMatrix
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

data class MatrixProjection(val rows: Int, val columns: Int) : (Int, Int) -> Int {
    override fun invoke(row: Int, column: Int): Int {
        require(row < rows && column < columns)
        return row * columns + column
    }

    val column = 0 until columns
    val row = 0 until rows

    val size = rows * columns

    val square get() = rows == columns
}

infix fun Int.by(columns: Int) = MatrixProjection(this, columns)

interface Matrix {
    val index: MatrixProjection

    operator fun get(row: Int, column: Int): BigDecimal
    fun map(transform: (BigDecimal) -> BigDecimal): Matrix
    fun round(scale: Int = 0): Matrix
    fun determinant(): BigDecimal

    fun inverse(): Matrix?

    infix fun x(other: Matrix): Matrix
    infix fun solve(rhs: Matrix): Matrix

    companion object {
        fun MatrixProjection.nullMatrix(): Matrix = this.mutableMatrix()
        fun matrix(vararg rows: List<Number>): Matrix = mutableMatrix(listOf(*rows))
        fun matrix(rows: List<List<Number>>): Matrix = mutableMatrix(rows)
        fun column(column: List<Number>): Matrix = matrix(column.map { listOf(it) })
        fun column(vararg column: Number): Matrix = column(listOf(*column))
    }
}

internal data class MutableMatrix(
    override val index: MatrixProjection,
    val data: MutableList<BigDecimal> = MutableList(index.size) { BigDecimal.ZERO },
    val divisionMode: MathContext = MathContext.DECIMAL128
) : Matrix {
    init {
        require(data.size == index.size) { "data list is to small, don't use the second parameter!" }
    }

    companion object {
        fun MatrixProjection.mutableMatrix(data: List<List<Number>>): MutableMatrix = MutableMatrix(
            this, data.flatten().map { it.value() }.toMutableList()
        )

        fun MatrixProjection.mutableMatrix() = MutableMatrix(this)

        fun mutableMatrix(rows: List<List<Number>>) = (rows.size by rows[0].size)
            .mutableMatrix(rows)

        fun column(column: List<Number>) = mutableMatrix(column.map { listOf(it) })

        private fun Number.value(): BigDecimal = when (this) {
            is Double -> BigDecimal.valueOf(toDouble())
            is Float -> BigDecimal.valueOf(toDouble())
            is BigDecimal -> this
            is BigInteger -> this.toBigDecimal()
            else -> BigDecimal.valueOf(toLong())
        }

        private infix fun BigDecimal.eq(o: BigDecimal) =
            this.compareTo(o) == 0

        fun diagonal(size: Int) = (size by size).mutableMatrix().apply {
            for (i in 0 until size) this[i, i] = BigDecimal.ONE
        }
    }

    override operator fun get(row: Int, column: Int): BigDecimal {
        return data[index(row, column)]
    }

    operator fun set(row: Int, column: Int, value: Number) {
        data[index(row, column)] = value.value()
    }

    fun copy() = copy(data = ArrayList(data))
    override fun map(transform: (BigDecimal) -> BigDecimal) =
        copy(data = data.map(transform).toMutableList())

    override fun round(scale: Int) =
        map { it.setScale(scale, RoundingMode.HALF_UP) }

    override fun determinant(): BigDecimal {
        require(index.square)

        if (index.rows == 1) return this[0, 0]
        if (index.rows == 2) return (this[0, 0] * this[1, 1] - this[0, 1] * this[1, 0])

        var det = BigDecimal.ZERO
        for (c in index.column) {
            var incValue = BigDecimal.ONE
            var decValue = BigDecimal.ONE

            for (r in index.row) {
                incValue *= this[r, (c + r) % index.columns]
                decValue *= this[index.rows - r - 1, (c + r) % index.columns]
            }
            det += (incValue - decValue)
        }
        return det
    }

    // adapted thx to https://dionsapoetra.medium.com/solving-linear-equation-system-a9f13e0a96c5
    // see https://github.com/dionsaputra/matrix-algeo
    override fun inverse(): MutableMatrix? {
        require(index.square) { "LHS matrix must be square" }

        if (determinant() eq BigDecimal.ZERO) return null

        val inverse = diagonal(index.rows)
        val temp = copy()

        for (fdRow in index.row) {
            var fdCol = 0

            while (fdCol < index.columns && temp[fdRow, fdCol] eq BigDecimal.ZERO) fdCol++

            // all-zero row does not work
            if (fdCol == index.columns) return null

            // divide this row by pivot value
            val scalingFactor = temp[fdRow, fdCol]
            for (c in index.column) {
                temp[fdRow, c] = temp[fdRow, c].divide(scalingFactor, divisionMode)
                inverse[fdRow, c] = inverse[fdRow, c].divide(scalingFactor, divisionMode)
            }

            // subtract with k*R
            for (r in index.row) {
                if (r == fdRow) continue
                val subFactor = temp[r, fdCol]
                for (c in index.column) {
                    temp[r, c] -= subFactor * temp[fdRow, c]
                    inverse[r, c] -= subFactor * inverse[fdRow, c]
                }
            }
        }

        return inverse
    }

    override infix fun x(other: Matrix): MutableMatrix {
        require(index.columns == other.index.rows)
        val res = (index.rows by other.index.columns).mutableMatrix()
        for (row in index.row) {
            for (column in other.index.column) {
                for (k in index.column) {
                    res[row, column] += this[row, k] * other[k, column]
                }
            }
        }
        return res
    }

    override fun toString(): String {
        return "[ " +
                index.row.joinToString(",\n ") { row ->
                    index.column.joinToString(", ") { col -> this[row, col].toString() }
                } + " ]"
    }

    override infix fun solve(rhs: Matrix) =
        inverse()?.let { it x rhs } ?: throw IllegalArgumentException("cannot solve $this vs $rhs")
}