package util

import util.Rectangle.Companion.asExtent

data class Grid<T>(private val grid: List<List<T>>) {
    val size: Point = Point(grid.maxOf { it.size }, grid.size)
    val extent = size.asExtent

    operator fun get(p: Point) = get(p.int.x, p.int.y)
    operator fun get(x: Int, y: Int): T? {
        return grid.getOrNull(y)?.getOrNull(x)
    }

    operator fun plus(cell: Pair<Point, T>) = copy(
        grid = grid.replace(cell.first.int.y) { row -> row.replace(cell.first.int.x) { cell.second } }
    )

    // maybe be implemented more efficiently
    operator fun plus(map: Map<Point, T>) =
        map.asSequence().fold(this) { g, e -> g + (e.key to e.value) }

    fun findAll(vararg v: T) = findAll { it in v }

    fun findAll(predicate: (T) -> Boolean) = grid.flatMapIndexed { y, row ->
        row.flatMapIndexed { x, value ->
            if (predicate(value)) listOf(Point(x, y) to value)
            else emptyList()
        }
    }

    fun findAllNearby(predicate: (T) -> Boolean, point: Point): List<Point> {
        return point.env().filter { get(it)?.let(predicate) ?: false }.toList()
    }

    fun <R> map(transform: (T) -> R) = Grid(grid = grid.map { it.map(transform) })
    fun <R> flatMap(transform: (T) -> Iterable<R>) = Grid(grid = grid.map { it.flatMap(transform) })

    companion object {
        fun charGridOf(text: String) = text.lines().map(String::toList).let(::Grid)

        fun Grid<Char>.asText(): String {
            return grid.joinToString("\n") { row ->
                row.joinToString("")
            }
        }
    }
}