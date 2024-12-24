package y2024.day15

import util.CardinalDirection
import util.Grid
import util.Grid.Companion.asText
import util.Point

data class Warehouse(
    val grid: Grid<Tile>
) {
    val robot: Point = grid.findAll(Robot).first().first
    val boxes: Set<Point> by lazy { grid.findAll(Box).map { it.first }.toSet() }
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
        val to = from.go(direction)
        return when {
            grid[to] == Box -> {
                val moves = tryMove(Box, to, direction)
                if (moves.isEmpty()) return emptyMap()
                else moves + mapOf(from to Empty, to to thing)
            }

            grid[to] == Empty -> mapOf(from to Empty, to to thing)
            else -> emptyMap()
        }
    }

    override fun toString(): String {
        return grid.map {
            when (it) {
                Robot -> '@'
                Empty -> '.'
                Box -> 'O'
                Wall -> '#'
            }
        }.asText()
    }


    companion object {
        fun parse(text: String): Warehouse {
            val grid = Grid.charGridOf(text).map {
                when (it) {
                    '@' -> Robot
                    '#' -> Wall
                    'O' -> Box
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

        fun execute(wareHouseAndDirections: String): Warehouse {
            val wareHouseText = wareHouseAndDirections.lineSequence().takeWhile { it.isNotBlank() }
                .joinToString("\n")
            val warehouse = parse(wareHouseText)
            val directionsText =
                wareHouseAndDirections.lineSequence().dropWhile { it.isNotBlank() }.drop(1)
                    .joinToString("")
            val directions = directions(directionsText)
            return warehouse.moveRobot(directions)
        }
    }

    sealed interface Tile
    sealed interface Movable : Tile
    data object Robot : Movable
    data object Box : Movable
    data object Wall : Tile
    data object Empty : Tile

}