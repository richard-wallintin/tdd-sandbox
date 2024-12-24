package y2024.day15

import util.CardinalDirection
import util.Grid
import util.Grid.Companion.asText
import util.Point

data class Warehouse(
    val grid: Grid<Tile>
) {
    val robot: Point = grid.findAll(Robot).first().first
    val boxes: Set<Point> by lazy {
        grid.findAll { it is SmallBox || it is BoxL }.map { it.first }.toSet()
    }
    val sumOfGPS: Long by lazy {
        boxes.sumOf { (x, y) ->
            x + 100 * y
        }
    }

    fun moveRobot(vararg direction: CardinalDirection): Warehouse =
        direction.fold(this, Warehouse::moveRobot)

    fun moveRobot(directions: Sequence<CardinalDirection>): Warehouse =
        directions.fold(this, Warehouse::moveRobot)

    fun moveRobot(direction: CardinalDirection) = copy(grid = grid + findMoves(direction))

    private fun findMoves(direction: CardinalDirection): Map<Point, Tile> {
        return tryMove(Robot, robot, direction)
    }

    private fun tryMove(
        thing: Movable,
        from: Point,
        direction: CardinalDirection
    ): Map<Point, Tile> {
        return if (thing is HalfBox && direction.vertical) {
            val l = if (thing is BoxL) from else from.go(CardinalDirection.W)
            val r = if (thing is BoxR) from else from.go(CardinalDirection.E)

            val tryLeft = tryToPush(BoxL, l, direction)
            val tryRight = tryToPush(BoxR, r, direction)

            if (tryLeft.isEmpty() || tryRight.isEmpty()) emptyMap()
            else {
                val freeSpots =
                    (tryLeft.filterValues { it is Empty }) + (tryRight.filterValues { it is Empty })
                val occupiedSpots =
                    (tryLeft.filterValues { it !is Empty }) + (tryRight.filterValues { it !is Empty })
                freeSpots + occupiedSpots
            }
        } else
            tryToPush(thing, from, direction)

    }

    private fun tryToPush(
        thing: Movable,
        from: Point,
        direction: CardinalDirection
    ): Map<Point, Tile> {
        val to = from.go(direction)
        return when (val other = grid[to]) {
            is Box -> {
                val moves = tryMove(other, to, direction)
                if (moves.isEmpty()) emptyMap()
                else moves + mapOf(from to Empty, to to thing)
            }

            Empty -> mapOf(from to Empty, to to thing)
            else -> emptyMap()
        }
    }

    override fun toString(): String {
        return grid.map {
            when (it) {
                Robot -> '@'
                Empty -> '.'
                SmallBox -> 'O'
                BoxL -> '['
                BoxR -> ']'
                Wall -> '#'
            }
        }.asText()
    }

    fun scaleUp(): Warehouse {
        return copy(grid = grid.flatMap {
            when (it) {
                Wall -> listOf(Wall, Wall)
                is Box -> listOf(BoxL, BoxR)
                Empty -> listOf(Empty, Empty)
                Robot -> listOf(Robot, Empty)
            }
        })
    }


    companion object {
        fun parse(text: String): Warehouse {
            val grid = Grid.charGridOf(text).map {
                when (it) {
                    '@' -> Robot
                    '#' -> Wall
                    'O' -> SmallBox
                    '[' -> BoxL
                    ']' -> BoxR
                    else -> Empty
                }
            }
            return Warehouse(
                grid
            )
        }

        fun directions(text: String) = text.asSequence()
            .filter { !it.isWhitespace() }
            .map { CardinalDirection.of(it.toString()) }

        fun execute(scaled: Boolean = false, mapAndDirections: String): Warehouse {
            val wareHouseText = mapAndDirections.lineSequence().takeWhile { it.isNotBlank() }
                .joinToString("\n")

            val warehouse = parse(wareHouseText).let {
                if (scaled) it.scaleUp() else it
            }

            val directionsText =
                mapAndDirections.lineSequence().dropWhile { it.isNotBlank() }.drop(1)
                    .joinToString("")
            val directions = directions(directionsText)
            return warehouse.moveRobot(directions)
        }
    }

    sealed interface Tile
    sealed interface Movable : Tile
    data object Robot : Movable

    sealed interface Box : Movable
    data object SmallBox : Box
    sealed interface HalfBox : Box
    data object BoxL : HalfBox
    data object BoxR : HalfBox

    data object Wall : Tile
    data object Empty : Tile
}