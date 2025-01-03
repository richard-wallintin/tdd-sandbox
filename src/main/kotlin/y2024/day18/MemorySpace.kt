package y2024.day18

import util.GridPath
import util.Point

data class MemorySpace(
    val size: Point,
    val corrupted: Set<Point> = emptySet(),
    val lastDrop: Point? = null,
) {
    private val bottomRight = size - Point.UNIT

    fun drop(byte: Point) = copy(corrupted = corrupted + byte, lastDrop = byte)
    fun isSafe(l: Point) = l in size && !corrupted.contains(l)

    fun dropAll(bytes: Sequence<Point>) = bytes.fold(this, MemorySpace::drop)

    fun shortestPath(from: Point = Point.ORIGIN, to: Point = bottomRight): Int? {
        return GridPath.start(from).shortestPaths(to, this::isSafe)
            .firstOrNull()?.length
    }

    companion object {
        fun readPoints(text: String) =
            text.lineSequence().map { it.split(',', limit = 2).map(String::toInt) }
                .map { (x, y) -> Point(x, y) }
    }
}