package y2024.day15

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import util.CardinalDirection.*
import util.Point

class WarehouseTest {

    private val smallSampleWarehouse = Warehouse.parse(
        """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
        """.trimIndent()
    )

    @Test
    fun `parse warehouse`() {
        smallSampleWarehouse.robot shouldBe Point(2, 2)
        smallSampleWarehouse.boxes.size shouldBe 6
    }

    @Test
    fun `try to move robot into wall has no effect`() {
        smallSampleWarehouse.moveRobot(W) shouldBe smallSampleWarehouse
    }

    @Test
    fun `move robot onto free tile`() {
        smallSampleWarehouse.moveRobot(N) shouldBe Warehouse.parse(
            """
            ########
            #.@O.O.#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
        """.trimIndent()
        )
    }

    @Test
    fun `move a box`() {
        smallSampleWarehouse.moveRobot(N, E) shouldBe Warehouse.parse(
            """
            ########
            #..@OO.#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
        """.trimIndent()
        )
    }

    @Test
    fun `move many boxes`() {
        smallSampleWarehouse.moveRobot(N, E, E) shouldBe Warehouse.parse(
            """
            ########
            #...@OO#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
        """.trimIndent()
        )
    }

    @Test
    fun `cannot move many boxes into wall`() {
        smallSampleWarehouse.moveRobot(N, E, E, E) shouldBe Warehouse.parse(
            """
            ########
            #...@OO#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
        """.trimIndent()
        )
    }

    private val smallSampleResult = Warehouse.parse(
        """
                ########
                #....OO#
                ##.....#
                #.....O#
                #.#O@..#
                #...O..#
                #...O..#
                ########
            """.trimIndent()
    )

    @Test
    fun `while seqeunce of moves`() {
        smallSampleWarehouse.moveRobot(
            Warehouse.directions("<^^>>>vv<v>>v<<")
        ) shouldBe smallSampleResult
    }


    private val largeExampleResult = Warehouse.parse(
        """
                ##########
                #.O.O.OOO#
                #........#
                #OO......#
                #OO@.....#
                #O#.....O#
                #O.....OO#
                #O.....OO#
                #OO....OO#
                ##########
            """.trimIndent()
    )

    @Test
    fun `larger example`() {
        Warehouse.execute(
            """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########
            
            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent()
        ) shouldBe largeExampleResult
    }

    @Test
    fun `gps sum`() {
        largeExampleResult.sumOfGPS shouldBe 10092
        smallSampleResult.sumOfGPS shouldBe 2028
    }

    @Test
    fun part1() {
        Warehouse.execute(AOC.getInput("/2024/day15.txt")).sumOfGPS shouldBe 1429911L
    }
}


