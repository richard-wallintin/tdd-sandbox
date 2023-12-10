package day10

data class Point(val x: Int, val y: Int) {
    fun go(dir: Direction) = Point(x + dir.x, y + dir.y)
}