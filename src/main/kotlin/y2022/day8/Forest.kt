package y2022.day8

data class Forest(val h: List<List<Int>> = listOf()) {
    val rows = h.size
    val columns = h[0].size

    init {
        assert(columns == rows)
        assert(h.all { it.size == columns })
    }

    fun isVisible(row: Int, col: Int): Boolean {
        if (edge(row, col)) return true

        val height = h[row][col]

        val left = b(col, row, height)
        val top = toTheTop(row, col).all { it < height }
        val right = toTheRight(col, row).all { it < height }
        val bottom = toTheBottom(row, col).all { it < height }

        return left || top || right || bottom
    }

    private fun edge(row: Int, col: Int) =
        row == 0 || row == rows - 1 || col == 0 || col == columns - 1

    private fun toTheTop(row: Int, col: Int) = (0 until row).map { h[it][col] }.reversed()
    private fun toTheBottom(row: Int, col: Int) = (row + 1 until rows).map { h[it][col] }
    private fun toTheRight(col: Int, row: Int) = (col + 1 until columns).map { h[row][it] }
    private fun toTheLeft(col: Int, row: Int) = (0 until col).map { h[row][it] }.reversed()

    fun scenicScore(row: Int, col: Int): Int {
        if (edge(row, col)) return 0

        val height = h[row][col]

        val left = toTheLeft(col, row).viewingDistance(height)
        val top = toTheTop(row, col).viewingDistance(height)
        val right = toTheRight(col, row).viewingDistance(height)
        val bottom = toTheBottom(row, col).viewingDistance(height)

        return left * top * right * bottom
    }

    private fun List<Int>.viewingDistance(height: Int): Int {
        val block = indexOfFirst { it >= height }
        return if (block >= 0) block + 1 else size
    }

    private fun b(col: Int, row: Int, height: Int) =
        toTheLeft(col, row).all { it < height }


    private fun all() = sequence {
        (0 until rows).forEach { r ->
            (0 until columns).forEach { c ->
                yield(r to c)
            }
        }
    }

    val visibleTreeCount: Int by lazy {
        all().map { (r, c) -> if (isVisible(r, c)) 1 else 0 }.sum()
    }

    val maxScenicScore: Int by lazy {
        all().map { (r, c) -> scenicScore(r, c) }.max()
    }


    companion object {
        fun of(grid: String): Forest {
            return grid.lineSequence().map { line ->
                line.asSequence().map { it.toString().toInt() }.toList()
            }.toList().let { Forest(it) }
        }
    }
}
