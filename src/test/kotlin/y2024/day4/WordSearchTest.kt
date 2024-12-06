package y2024.day4

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.Grid
import util.Point

class WordSearchTest {


    private val sampleMatrixSmall = WordSearchMatrix.parse(
        """
                ..X...
                .SAMX.
                .A..A.
                XMAS.S
                .X....
            """.trimIndent()
    )

    private val sampleMatrixMedium = WordSearchMatrix.parse(
        """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent()
    )

    @Test
    fun `create a word search matrix`() {
        sampleMatrixSmall.get(0, 2) shouldBe 'X'
        sampleMatrixSmall.get(1, 1) shouldBe 'S'
    }

    @Test
    fun `find all Xes`() {
        sampleMatrixSmall.findAll('X') shouldBe listOf(
            Point(2, 0), Point(4, 1), Point(0, 3), Point(1, 4)
        )
    }

    @Test
    fun `check all Neighbours`() {
        sampleMatrixSmall.findAllNearby('M', Point(0, 2)) shouldBe listOf(
            Point(1, 3)
        )
    }

    @Test
    fun `find all XMASes`() {
        sampleMatrixSmall.findXMASes() shouldBe 4
        sampleMatrixMedium.findXMASes() shouldBe 18
    }

    private val inputMatrix = WordSearchMatrix.parse(AOC.getInput("/2024/day4.txt"))

    @Test
    fun part1() {
        inputMatrix.findXMASes() shouldBe 2583
    }

    @Test
    fun `find all X-MASes`() {
        sampleMatrixMedium.findAllCrossMASes() shouldBe 9
    }

    @Test
    fun part2() {
        inputMatrix.findAllCrossMASes() shouldBe 1978
    }
}

data class WordSearchMatrix(private val grid: Grid<Char>) {
    constructor(characters: List<List<Char>>) : this(Grid(characters))

    fun get(p: Point) = grid[p] ?: '.'
    fun get(row: Int, col: Int) = grid[col, row]

    fun findAll(c: Char) = grid.findAll(c).map { it.first }
    fun findAllNearby(c: Char, point: Point) =
        grid.findAllNearby({ it == c }, point)

    fun findXMASes(): Int {
        var count = 0
        findAll('X').forEach { pointX ->
            findAllNearby('M', pointX).forEach { pointM ->
                val direction = pointM - pointX

                val pointA = pointM + direction
                val pointS = pointA + direction

                if (get(pointA) == 'A' && get(pointS) == 'S') {
                    count++
                }
            }
        }
        return count
    }

    fun findAllCrossMASes(): Int {
        var count = 0
        findAll('A').forEach { pointA ->
            val pointMs = listOf(Point(1, 1), Point(1, -1), Point(-1, -1), Point(-1, 1)).count {
                val pointM = pointA + it
                val pointS = pointA - it
                get(pointM) == 'M' && get(pointS) == 'S'
            }

            if (pointMs == 2) count++
        }
        return count
    }

    companion object {
        fun parse(text: String): WordSearchMatrix {
            return WordSearchMatrix(text.lines().map { it.toList() })
        }
    }

}
