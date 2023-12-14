package day11

import kotlin.math.abs

data class Universe(
    val rows: Int,
    val columns: Int,
    val galaxyCoordinates: List<Coord>,
) {
    val galaxies = galaxyCoordinates.size
    fun locationOfGalaxy(no: Int): Coord {
        return galaxyCoordinates[no - 1]
    }

    fun shortestPath(noA: Int, noB: Int): Int {
        val a = locationOfGalaxy(noA)
        val b = locationOfGalaxy(noB)
        return distance(b, a)
    }

    private fun distance(b: Coord, a: Coord) = abs(b.row - a.row) + abs(b.col - a.col)

    fun expand(): Universe {
        return Universe(
            rows = rows + emptyRows,
            columns = columns + emptyColumns,
            galaxyCoordinates = galaxyCoordinates.map { original ->
                Coord(
                    row = expandRow(original.row),
                    col = expandCol(original.col)
                )
            }
        )
    }

    private fun expandCol(col: Int): Int {
        return emptyColumnNumbers.count { it <= col } + col
    }

    private fun expandRow(row: Int): Int {
        return emptyRowNumbers.count { it <= row } + row
    }

    fun totalShortestPaths(): Int {
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
            var rows = 0
            var cols = 0
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
