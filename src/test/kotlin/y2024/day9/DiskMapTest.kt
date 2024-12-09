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

    private val sampleMap = DiskMap.parse("2333133121414131402")

    @Test
    fun `checksum computation`() {
        sampleMap.compact().checksum shouldBe 1928
    }

    private val inputMap = DiskMap.parse(AOC.getInput("/2024/day9.txt"))

    @Test
    fun part1() {
        inputMap.compact().checksum shouldBe 6607511583593L
    }

    @Test
    fun `detect files and free spans`() {
        shortSampleMap.spans().toList() shouldBe listOf(
            FileSpan(fileId = 0, 1),
            Gap(size = 2),
            FileSpan(fileId = 1, size = 3),
            Gap(size = 4),
            FileSpan(fileId = 2, size = 5)
        )
    }

    @Test
    fun compactWholeFilesOnly() {
        val compacted = sampleMap.compactWholeFiles()
        compacted.spans().toList() shouldBe listOf(
            //00992111777.44.333....5555.6666.....8888..
            FileSpan(fileId = 0, size = 2),
            FileSpan(fileId = 9, size = 2),
            FileSpan(fileId = 2, size = 1),
            FileSpan(fileId = 1, size = 3),
            FileSpan(fileId = 7, size = 3),
            Gap(size = 1),
            FileSpan(fileId = 4, size = 2),
            Gap(size = 1),
            FileSpan(fileId = 3, size = 3),
            Gap(size = 4),
            FileSpan(fileId = 5, size = 4),
            Gap(size = 1),
            FileSpan(fileId = 6, size = 4),
            Gap(size = 5),
            FileSpan(fileId = 8, size = 4),
            Gap(size = 2)
        )

        compacted.checksum shouldBe 2858
    }

    @Test
    fun part2() {
        inputMap.compactWholeFiles().checksum shouldBe 6636608781232L
    }
}