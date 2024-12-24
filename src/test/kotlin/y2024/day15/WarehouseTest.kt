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
            mapAndDirections = """
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

    private val input = AOC.getInput("/2024/day15.txt")

    @Test
    fun part1() {
        Warehouse.execute(mapAndDirections = input).sumOfGPS shouldBe 1429911L
    }

    private val smallSampleScaled = Warehouse.parse(
        """
                #######
                #...#.#
                #.....#
                #..OO@#
                #..O..#
                #.....#
                #######
            """.trimIndent()
    ).scaleUp()

    @Test
    fun `parse and scale`() {
        smallSampleScaled.toString() shouldBe """
            ##############
            ##......##..##
            ##..........##
            ##....[][]@.##
            ##....[]....##
            ##..........##
            ##############
        """.trimIndent()
    }

    @Test
    fun `correct box count`() {
        smallSampleScaled.boxes shouldBe setOf(
            Point(6, 3),
            Point(6, 4),
            Point(8, 3),
        )
    }

    @Test
    fun `execute moves`() {
        smallSampleScaled.moveRobot(Warehouse.directions("<vv<<^^<<^^")).toString() shouldBe """
            ##############
            ##...[].##..##
            ##...@.[]...##
            ##....[]....##
            ##..........##
            ##..........##
            ##############
        """.trimIndent()
    }

    private val largeSampleScaledResult = Warehouse.parse(
        """
                ####################
                ##[].......[].[][]##
                ##[]...........[].##
                ##[]........[][][]##
                ##[]......[]....[]##
                ##..##......[]....##
                ##..[]............##
                ##..@......[].[][]##
                ##......[][]..[]..##
                ####################
            """.trimIndent()
    )

    @Test
    fun `larger sample scaled also works`() {
        Warehouse.execute(
            scaled = true,
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
        ) shouldBe largeSampleScaledResult
    }

    @Test
    fun `correct scoring for scaled`() {
        largeSampleScaledResult.sumOfGPS shouldBe 9021
    }

    @Test
    fun part2() {
        Warehouse.execute(scaled = true, mapAndDirections = input).sumOfGPS shouldBe 1453087L
    }
}


