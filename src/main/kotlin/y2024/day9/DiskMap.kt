package y2024.day9

import util.swap
import util.times

sealed interface Block
data object FreeBlock : Block
data class FileBlock(val fileId: Int) : Block

data class DiskMap(val blocks: List<Block>) {
    val isCompact: Boolean by lazy {
        val firstFree = blocks.indexOfFirst { it is FreeBlock }
        if (firstFree >= 0)
            blocks.drop(firstFree).all { it is FreeBlock }
        else true
    }

    val checksum: Long by lazy {
        blocks.filterIsInstance<FileBlock>().mapIndexed { i, f -> i.toLong() * f.fileId }.sum()
    }

    fun compactStep(): DiskMap {
        val blocks = blocks.toMutableList()

        val free = blocks.indexOfFirst { it is FreeBlock }
        val file = blocks.indexOfLast { it is FileBlock }
        blocks.swap(free, file)

        return copy(blocks = blocks.toList())
    }

    fun compact(): DiskMap {
        val blocks = blocks.toMutableList()

        while (true) {
            val free = blocks.indexOfFirst { it is FreeBlock }
            val file = blocks.indexOfLast { it is FileBlock }
            check(file >= 0)
            if (free < file)
                blocks.swap(free, file)
            else
                return copy(blocks = blocks.toList())
        }
    }

    companion object {
        fun parse(line: String): DiskMap {
            var isFile = true
            var nextId = 0
            return DiskMap(line.flatMap { c ->
                val size = "$c".toInt()
                if (isFile) {
                    isFile = false
                    val fileId = nextId++
                    size.times(FileBlock(fileId = fileId))
                } else {
                    isFile = true
                    size.times(FreeBlock)
                }
            })
        }
    }

}

