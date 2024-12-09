package y2024.day9

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DiskMapTest {

    private val shortSampleMap = DiskMap.parse("12345")

    @Test
    fun `parse disk map`() {
        // 0..111....22222
        shortSampleMap shouldBe DiskMap(
            listOf(
                FileBlock(0),
                FreeBlock,
                FreeBlock,
                FileBlock(1),
                FileBlock(1),
                FileBlock(1),
                FreeBlock,
                FreeBlock,
                FreeBlock,
                FreeBlock,
                FileBlock(2),
                FileBlock(2),
                FileBlock(2),
                FileBlock(2),
                FileBlock(2)
            )
        )
    }

    @Test
    fun `compact single step`() {
        // 02.111....2222.
        shortSampleMap.compactStep() shouldBe DiskMap(
            listOf(
                FileBlock(0),
                FileBlock(2),
                FreeBlock,
                FileBlock(1),
                FileBlock(1),
                FileBlock(1),
                FreeBlock,
                FreeBlock,
                FreeBlock,
                FreeBlock,
                FileBlock(2),
                FileBlock(2),
                FileBlock(2),
                FileBlock(2),
                FreeBlock
            )
        )
    }

    @Test
    fun `detect compacted`() {
        shortSampleMap.isCompact shouldBe false
        DiskMap(listOf(FileBlock(0), FreeBlock)).isCompact shouldBe true
    }

    @Test
    fun `compact until compacted`() {
        shortSampleMap.compact() shouldBe DiskMap(
            listOf(
                FileBlock(0),
                FileBlock(2),
                FileBlock(2),
                FileBlock(1),
                FileBlock(1),
                FileBlock(1),
                FileBlock(2),
                FileBlock(2),
                FileBlock(2),
                FreeBlock,
                FreeBlock,
                FreeBlock,
                FreeBlock,
                FreeBlock,
                FreeBlock
            )
        )
    }

    @Test
    fun `checksum computation`() {
        DiskMap.parse("2333133121414131402").compact().checksum shouldBe 1928
    }

    @Test
    fun part1() {
        DiskMap.parse(AOC.getInput("/2024/day9.txt")).compact().checksum shouldBe 6607511583593L
    }
}