package y2024

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
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

    private val sampleMatrixMedium = WordSearchMatrix.parse("""
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
    """.trimIndent())

    @Test
    fun `create a word search matrix`() {
        sampleMatrixSmall.get(0, 2) shouldBe 'X'
        sampleMatrixSmall.get(1, 1) shouldBe 'S'
    }

    @Test
    fun `find all Xes`() {
        sampleMatrixSmall.findAll('X') shouldBe listOf(
            Point(0, 2), Point(1, 4), Point(3, 0), Point(4, 1)
        )
    }

    @Test
    fun `check all Neighbours`() {
        sampleMatrixSmall.findAllNearby('M', Point(0, 2)) shouldBe listOf(
            Point(1,3)
        )
    }

    @Test
    fun `find all XMASes`() {
        sampleMatrixSmall.findXMASes() shouldBe 4
        sampleMatrixMedium.findXMASes() shouldBe 18
    }

    @Test
    fun part1() {
        WordSearchMatrix.parse(AOC.getInput("/2024/day4.txt")).findXMASes() shouldBe 2583
    }
}

class WordSearchMatrix(private val characters: List<List<Char>>) {
    fun get(p: Point) = get(p.int.x, p.int.y)

    fun get(row: Int, col: Int): Char {
        return characters.getOrNull(row)?.getOrNull(col) ?: '.'
    }

    fun findAll(c: Char): List<Point> {
        return characters.flatMapIndexed { rowIndex, row ->
            row.flatMapIndexed { colIndex, char ->
                if(char == c) listOf(Point(rowIndex,colIndex))
                else emptyList()
            }
        }
    }

    fun findAllNearby(c: Char, point: Point): List<Point> {
        return point.env().filter { get(it.int.x,it.int.y) == c }.toList()
    }

    fun findXMASes(): Int {
        var count = 0
        findAll('X').forEach { pointX ->
            findAllNearby('M', pointX).forEach { pointM ->
                val direction = pointM - pointX

                val pointA = pointM + direction
                val pointS = pointA + direction

                if( get(pointA) == 'A' && get(pointS) == 'S' ){
                    count++
                }
            }
        }
        return count
    }

    companion object {
        fun parse(text: String): WordSearchMatrix {
            return WordSearchMatrix(text.lines().map { it.toList() })
        }
    }

}
