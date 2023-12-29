package y2023.day22

data class BrickStack(val settled: List<Brick>) {
    private val supportMap: Map<Brick, List<Brick>> by lazy {
        sequence {
            var remaining = settled
            while (remaining.isNotEmpty()) {
                val brick = remaining.first()
                remaining = remaining.drop(1)
                yield(brick to brick.support(remaining))
            }
        }.toMap()
    }
    private val supportedCountMap = mutableMapOf<Brick, Int>()

    fun support(b: Brick) = supportMap[b] ?: emptyList()
    fun supportedCount(b: Brick) = supportedCountMap.getOrPut(b) {
        supportMap.values.count { b in it }
    }

    fun safeCount() = settled.count { safeCount(it) }

    private fun safeCount(b: Brick): Boolean {
        return support(b).all { supportedCount(it) > 1 }
    }

    companion object {
        fun of(bricks: List<Brick>) = BrickStack(bricks.settle())

        fun List<Brick>.settle(): List<Brick> {
            val settled = mutableListOf<Brick>()

            sortedBy { it.bottom }.forEach { fallingBrick ->
                val fallenBrick = settled.filter { it.horizontalOverlap(fallingBrick) }.maxByOrNull { it.top }?.let {
                    fallingBrick.elevateTo(it.top + 1)
                } ?: fallingBrick.elevateTo(1)

                settled.add(fallenBrick)
            }

            return settled
        }
    }
}
