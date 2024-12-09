package y2024.day9

import util.replace
import util.swap
import util.times

sealed interface Block
data object FreeBlock : Block
data class FileBlock(val fileId: Int) : Block

sealed interface Span {
    fun asBlocks(): Iterable<Block>

    val size: Int
}

data class FileSpan(val fileId: Int, override val size: Int) : Span {
    override fun asBlocks() = size.times(FileBlock(fileId))
    fun toGap() = Gap(size)
}

data class Gap(override val size: Int) : Span {
    override fun asBlocks() = size.times(FreeBlock)
    fun remaining(file: FileSpan): Gap? {
        return if (file.size < size) Gap(size - file.size)
        else null
    }
}

data class DiskMap(val blocks: List<Block>) {
    val isCompact: Boolean by lazy {
        val firstFree = blocks.indexOfFirst { it is FreeBlock }
        if (firstFree >= 0)
            blocks.drop(firstFree).all { it is FreeBlock }
        else true
    }

    val checksum: Long by lazy {
        blocks.mapIndexed { i, f -> if (f is FileBlock) i.toLong() * f.fileId else 0L }.sum()
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

        var fileBlockIndex = blocks.size - 1
        while (fileBlockIndex > 0) {
            val file = blocks[fileBlockIndex]
            if (file is FileBlock) {
                val free = blocks.indexOfFirst { it is FreeBlock }
                if (free < fileBlockIndex)
                    blocks.swap(free, fileBlockIndex)
                else {
                    break
                }
            }
            fileBlockIndex--
        }
        return copy(blocks = blocks.toList())
    }

    fun spans() = sequence {
        var currentFileId = -1
        var currentSpanStart = 0
        blocks.asSequence().forEachIndexed { i, b ->
            if (currentFileId != -1) {
                if ((b is FileBlock && currentFileId != b.fileId) || b is FreeBlock) {
                    yield(
                        FileSpan(
                            fileId = currentFileId,
                            size = i - currentSpanStart
                        )
                    )

                    currentSpanStart = i
                    currentFileId = if (b is FileBlock) b.fileId else -1
                }
            } else if (b is FileBlock) {
                yield(Gap(size = i - currentSpanStart))
                currentFileId = b.fileId
                currentSpanStart = i
            }
        }

        if (currentFileId != -1) {
            yield(
                FileSpan(
                    fileId = currentFileId,
                    size = blocks.size - currentSpanStart
                )
            )
        } else {
            yield(Gap(size = blocks.size - currentSpanStart))
        }
    }.filter { it.size > 0 }

    fun compactWholeFiles(): DiskMap {
        val spans = spans().toMutableList()

        var fileIndex = spans.size - 1
        while (fileIndex > 0) {
            val file = spans[fileIndex]
            if (file is FileSpan) {
                val matchingGapIndex = spans.indexOfFirst { it is Gap && it.size >= file.size }
                if (matchingGapIndex in 0..<fileIndex) {
                    // "swap"
                    spans.replace(fileIndex, file.toGap())
                    val matchingGap = spans.replace(matchingGapIndex, file) as Gap
                    matchingGap.remaining(file)?.let {
                        spans.add(matchingGapIndex + 1, it)
                        fileIndex++
                    }
                }
            }
            fileIndex--
        }

        return DiskMap(spans.flatMap(Span::asBlocks))
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

