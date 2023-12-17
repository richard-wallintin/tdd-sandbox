package y2023.day11

import kotlin.math.abs

data class Universe(
    val rows: Long,
    val columns: Long,
    val galaxyCoordinates: List<Coord>,
) {
    val galaxies = galaxyCoordinates.size
    fun locationOfGalaxy(no: Int): Coord {
        return galaxyCoordinates[no - 1]
    }

    fun shortestPath(noA: Int, noB: Int): Long {
        val a = locationOfGalaxy(noA)
        val b = locationOfGalaxy(noB)
        return distance(b, a)
    }

    private fun distance(b: Coord, a: Coord) = abs(b.row - a.row) + abs(b.col - a.col)

    fun expand(factor: Long = 2): Universe {
        val expansionFactor = factor - 1
        return Universe(
            rows = rows + (emptyRows * expansionFactor),
            columns = columns + (emptyColumns * expansionFactor),
            galaxyCoordinates = galaxyCoordinates.map { original ->
                Coord(
                    row = original.row + (expansionRows(original.row) * expansionFactor),
                    col = original.col + (expansionColumns(original.col) * expansionFactor)
                )
            }
        )
    }

    private fun expansionColumns(col: Long) = emptyColumnNumbers.count { it <= col }.toLong()
    private fun expansionRows(row: Long) = emptyRowNumbers.count { it <= row }.toLong()

    fun totalShortestPaths(): Long {
        return galaxyCoordinates.mapIndexed { i, c ->
            galaxyCoordinates.drop(i + 1).sumOf { distance(c, it) }
        }.sum()
    }

    private val emptyRowNumbers by lazy {
        (0 until rows).filter {
            galaxyCoordinates.none { c -> c.row == it }
        }
    }

    private val emptyColumnNumbers by lazy {
        (0 until columns).filter {
            galaxyCoordinates.none { c -> c.col == it }
        }
    }

    val emptyRows get() = emptyRowNumbers.count()
    val emptyColumns get() = emptyColumnNumbers.count()

    companion object {
        fun parse(text: String): Universe {
            var rows = 0L
            var cols = 0L
            val galaxyCoordinates = mutableListOf<Coord>()
            text.lineSequence().forEach { line ->
                cols = 0
                line.asSequence().forEach { c ->
                    if (c == '#')
                        galaxyCoordinates.add(Coord(rows, cols))
                    cols++
                }
                rows++
            }


            return Universe(
                rows = rows,
                columns = cols,
                galaxyCoordinates = galaxyCoordinates.toList()
            )
        }
    }
}
