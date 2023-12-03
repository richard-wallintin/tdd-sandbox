package day3

import AOC
import day3.Gear.Companion.asGear
import day3.Location.Companion.by
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class EnginePartsTest {

    private val reference = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent()

    @Test
    fun `detect adjacent labels`() {
        PartNumber(no = 467, row = 0, col = 0..2).adjacent(
            location = 1 by 3
        ) shouldBe true

        PartNumber(no = 35, row = 2, col = 2..3).adjacent(
            location = 1 by 3
        ) shouldBe true

        PartNumber(no = 617, row = 4, col = 0..2).adjacent(
            location = 4 by 3
        ) shouldBe true
    }

    private val refSchematic = Schematic.read(reference)

    @Test
    fun `read schematic`() {
        refSchematic shouldBe Schematic(
            listOf(
                PartNumber(no = 467, row = 0, col = 0..2),
                PartNumber(no = 114, row = 0, col = 5..7),
                Part(symbol = "*", location = Location(row = 1, col = 3)),
                PartNumber(no = 35, row = 2, col = 2..3),
                PartNumber(no = 633, row = 2, col = 6..8),
                Part(symbol = "#", location = Location(row = 3, col = 6)),
                PartNumber(no = 617, row = 4, col = 0..2),
                Part(symbol = "*", location = Location(row = 4, col = 3)),
                Part(symbol = "+", location = Location(row = 5, col = 5)),
                PartNumber(no = 58, row = 5, col = 7..8),
                PartNumber(no = 592, row = 6, col = 2..4),
                PartNumber(no = 755, row = 7, col = 6..8),
                Part(symbol = "$", location = Location(row = 8, col = 3)),
                Part(symbol = "*", location = Location(row = 8, col = 5)),
                PartNumber(no = 664, row = 9, col = 1..3),
                PartNumber(no = 598, row = 9, col = 5..7)
            )
        )
    }

    @Test
    fun findPartNumbers() {
        refSchematic.findPartNumbers() shouldBe listOf(
            PartNumber(no = 467, row = 0, col = 0..2),
            PartNumber(no = 35, row = 2, col = 2..3),
            PartNumber(no = 633, row = 2, col = 6..8),
            PartNumber(no = 617, row = 4, col = 0..2),
            PartNumber(no = 592, row = 6, col = 2..4),
            PartNumber(no = 755, row = 7, col = 6..8),
            PartNumber(no = 664, row = 9, col = 1..3),
            PartNumber(no = 598, row = 9, col = 5..7)
        )
    }

    @Test
    fun `sum part numbers`() {
        refSchematic.sumPartNumbers() shouldBe 4361
    }

    private val schematic = Schematic.read(AOC.getInput("/day3.txt"))

    @Test
    fun `part 1`() {
        schematic.sumPartNumbers() shouldBe 532_428
    }

    @Test
    fun `identify gears`() {
        refSchematic.gears shouldBe listOf(
            Gear(
                LabeledPart(
                    part = Part(symbol = "*", location = Location(row = 1, col = 3)),
                    numbers = listOf(
                        PartNumber(no = 467, row = 0, col = 0..2),
                        PartNumber(no = 35, row = 2, col = 2..3),
                    )
                )
            ),
            Gear(
                LabeledPart(
                    part = Part(symbol = "*", location = Location(row = 8, col = 5)),
                    numbers = listOf(
                        PartNumber(no = 755, row = 7, col = 6..8),
                        PartNumber(no = 598, row = 9, col = 5..7)
                    )
                )
            )
        )
    }

    @Test
    fun `gear ratio`() {
        Gear(
            LabeledPart(
                part = Part(symbol = "*", location = Location(row = 8, col = 5)),
                numbers = listOf(
                    PartNumber(no = 755, row = 7, col = 6..8),
                    PartNumber(no = 598, row = 9, col = 5..7)
                )
            )
        ).ratio shouldBe 451_490
    }

    @Test
    fun `overall ratio ref`() {
        refSchematic.overallGearRatio shouldBe 467835
    }

    @Test
    fun `part two`() {
        schematic.overallGearRatio shouldBe 84_051_670
    }
}

data class Location(val row: Int, val col: Int) {
    companion object {
        infix fun Int.by(col: Int) = Location(this, col)
    }

}

data class GridArea(val rows: IntRange, val cols: IntRange) {
    operator fun contains(l: Location) = l.row in rows && l.col in cols
    fun grow() = copy(
        rows = rows.first - 1..rows.last + 1,
        cols = cols.first - 1..cols.last + 1
    )
}

sealed class SchematicElement

data class PartNumber(val no: Int, val row: Int, val col: IntRange) : SchematicElement() {
    private val area = GridArea(row..row, col).grow()
    fun adjacent(location: Location): Boolean {
        return location in area
    }
}

data class Part(val symbol: String, val location: Location) : SchematicElement()

data class LabeledPart(val part: Part, val numbers: List<PartNumber>)
data class Gear(val labeledPart: LabeledPart) {
    val ratio = labeledPart.numbers.map { it.no }.reduce(Int::times)

    companion object {
        fun LabeledPart.asGear() = if (part.symbol == "*" && numbers.size == 2) Gear(this) else null
    }
}

data class Schematic(val elements: List<SchematicElement> = listOf()) {
    val parts by lazy { allParts().groupBy { it.location } }

    private fun allParts() = elements.asSequence().filterIsInstance<Part>()
    private fun partNumberSequence() = elements.asSequence().filterIsInstance<PartNumber>()

    private fun labeledPartSequence() = allParts().map { part ->
        LabeledPart(
            part = part,
            numbers = partNumberSequence().filter { it.adjacent(part.location) }.toList()
        )
    }.filter { it.numbers.isNotEmpty() }

    fun findPartNumbers(): List<PartNumber> {
        return partNumberSequence().filter { pn ->
            parts.keys.any { pn.adjacent(it) }
        }.toList()
    }

    fun sumPartNumbers(): Int {
        return findPartNumbers().sumOf { it.no }
    }

    val gears by lazy {
        labeledPartSequence().mapNotNull { it.asGear() }.toList()
    }

    val overallGearRatio by lazy {
        gears.sumOf { it.ratio }
    }

    companion object {
        fun read(reference: String) =
            Schematic(reference.lineSequence().withIndex().flatMap { line ->
                Regex("\\d+|[^.]").findAll(line.value).map { match ->
                    match.value.toIntOrNull()
                        ?.let { PartNumber(no = it, row = line.index, col = match.range) }
                        ?: Part(symbol = match.value, line.index by match.range.first)
                }
            }.toList())
    }
}