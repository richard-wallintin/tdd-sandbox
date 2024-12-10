package util

data class Grid<T>(private val grid: List<List<T>>) {
    val size: Point = Point(grid.maxOf { it.size }, grid.size)

    operator fun get(p: Point) = get(p.int.x, p.int.y)
    operator fun get(x: Int, y: Int): T? {
        return grid.getOrNull(y)?.getOrNull(x)
    }

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

    companion object {
        fun charGridOf(text: String) = text.lines().map(String::toList).let(::Grid)
    }
}